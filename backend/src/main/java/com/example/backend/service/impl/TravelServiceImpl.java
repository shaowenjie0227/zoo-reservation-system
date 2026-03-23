package com.example.backend.service.impl;

import com.example.backend.entity.Travel;
import com.example.backend.entity.User;
import com.example.backend.entity.Weight;
import com.example.backend.entity.vo.travel.TravelVO;
import com.example.backend.mapper.TravelMapper;
import com.example.backend.service.ITravelService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.backend.service.IUserService;
import com.example.backend.service.IWeightService;
import org.springframework.stereotype.Service;
import cn.hutool.core.text.CharSequenceUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.backend.common.model.PageResult;
import com.example.backend.exception.BusinessException;
import org.springframework.beans.BeanUtils;

import javax.annotation.Resource;

import com.example.backend.common.enums.CodeEnum;

import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 旅游景点表 服务实现类
 * </p>
 *
 * @author
 */
@Service
public class TravelServiceImpl extends ServiceImpl<TravelMapper, Travel> implements ITravelService {
    @Resource
    private TravelMapper travelMapper;
    @Resource
    private IUserService userService;
    @Resource
    private IWeightService weightService;

    /**
     * 新增
     *
     * @param request
     * @return
     */
    @Override
    public Integer add(Travel request) {
        Travel saveData = new Travel();
        BeanUtils.copyProperties(request, saveData);
        travelMapper.insert(saveData);
        return saveData.getId();
    }

    /**
     * 批量新增
     *
     * @param request
     * @return
     */
    @Override
    public Boolean batchAdd(List<Travel> request) {
        return saveBatch(request);
    }

    /**
     * 删除
     *
     * @param id
     * @return
     */
    @Override
    public Boolean del(Integer id) {
        if (id == null || id <= 0) {
            throw new BusinessException(CodeEnum.PARAMS_ERROR);
        }
        return removeById(id);
    }

    /**
     * 批量删除
     *
     * @param ids 批量ID列表
     * @return 是否成功
     */
    @Override
    public Boolean batchDel(List<Integer> ids) {
        if (ids == null || ids.isEmpty()) {
            throw new BusinessException(CodeEnum.PARAMS_ERROR);
        }
        return removeByIds(ids);
    }

    /**
     * 编辑
     *
     * @param request 请求参数
     * @return 是否成功
     */
    @Override
    public Boolean edit(Travel request) {
        if (request.getId() == null || request.getId() <= 0) {
            throw new BusinessException(CodeEnum.PARAMS_ERROR);
        }
        return updateById(request);
    }

    /**
     * 分页查询
     *
     * @param pageNum  页码
     * @param pageSize 页大小
     * @param name     名称
     * @param location 位置
     * @return 分页结果
     */
    @Override
    public PageResult<List<TravelVO>> queryPage(Integer pageNum, Integer pageSize, String name, String location) {
        QueryWrapper<Travel> queryWrapper = new QueryWrapper<>();
        if (CharSequenceUtil.isNotBlank(name)) {
            queryWrapper.like("name", name);
        }
        if (CharSequenceUtil.isNotBlank(location)) {
            queryWrapper.like("location", location);
        }
        queryWrapper.orderByDesc("id");
        Page<Travel> page = new Page<>(pageNum, pageSize);
        Page<Travel> result = travelMapper.selectPage(page, queryWrapper);
        List<TravelVO> travelVOList = convertVO(result.getRecords());
        return new PageResult<>(travelVOList, result.getCurrent(), result.getSize(), result.getTotal());
    }

    /**
     * 查询所有
     *
     * @return 所有数据
     */
    @Override
    public List<Travel> getAll() {
        QueryWrapper<Travel> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("create_time");
        return travelMapper.selectList(queryWrapper);
    }

    /**
     * 根据ID查询详情
     *
     * @param id 主键ID
     * @return 详情信息
     */
    @Override
    public Travel getByIdDetail(Integer id) {
        if (id == null || id <= 0) {
            throw new BusinessException(CodeEnum.PARAMS_ERROR);
        }
        Travel travel = travelMapper.selectById(id);
        if (travel != null && travel.getViewCount() != null) {
            // 增加浏览数
            travel.setViewCount(travel.getViewCount() + 1);
            travelMapper.updateById(travel);
        }
        return travel;
    }

    /**
     * 转换为VO对象
     *
     * @param travels 实体列表
     * @return VO列表
     */
    private List<TravelVO> convertVO(List<Travel> travels) {
        List<TravelVO> travelVOList = new ArrayList<>();
        for (Travel travel : travels) {
            TravelVO travelVO = new TravelVO();
            BeanUtils.copyProperties(travel, travelVO);
            travelVOList.add(travelVO);
        }
        return travelVOList;
    }

    @Override
    public List<TravelVO> recommend(Integer num) {
        // 获取用户信息
        User user = userService.getLoginUserInfo();
        if (user == null) {
            // 如果用户未登录, 随机返回指定数量的景点
            return randomTravels(num);
        }
        Integer userId = user.getId();
        // 基于协同过滤算法实现推荐
        // 1. 计算所有用户之间的相似度矩阵
        Map<Integer, Map<Integer, Double>> similarityMatrix = calculateUserSimilarityMatrix();

        // 2. 获取与目标用户相似的其他用户
        Map<Integer, Double> similarUsers = similarityMatrix.getOrDefault(userId, new HashMap<>());

        // 打印初始评分矩阵
        Map<Integer, Map<Integer, Integer>> userItemMatrix = buildUserItemMatrix();

        // 3. 收集相似用户喜欢的景点，使用LinkedHashMap保持相似度权重
        Map<Integer, Double> recommendedWithWeight = new LinkedHashMap<>();
        // 只取相似度最高的10个用户
        List<Map.Entry<Integer, Double>> sortUsers = similarUsers.entrySet().stream()
                .filter(entry -> entry.getValue() > 0) // 相似度大于0
                .sorted(Map.Entry.<Integer, Double>comparingByValue().reversed()) // 按相似度降序排序
                .limit(10)
                .collect(Collectors.toList());

        System.out.println("\n2. 用户相似度结果==========");
        // 遍历相似用户列表并打印相似度（原格式）
        for (Map.Entry<Integer, Double> entry : sortUsers) {
            Integer similarUserId = entry.getKey();
            Double similarity = entry.getValue();
            System.out.println("与用户序号: " + similarUserId + "的余弦度相似度---->" + similarity);
        }

        // 遍历取出相似用户喜欢的景点，按相似度权重收集
        sortUsers.forEach(entry -> {
            Integer similarUserId = entry.getKey();
            Double similarity = entry.getValue();
            QueryWrapper<Weight> wrapper = new QueryWrapper<>();
            wrapper.eq("user_id", similarUserId);
            List<Weight> behaviors = weightService.list(wrapper);
            behaviors.forEach(b -> {
                Integer travelId = b.getTravelId();
                // 如果景点已存在，保留更高的相似度权重
                if (!recommendedWithWeight.containsKey(travelId) || 
                    recommendedWithWeight.get(travelId) < similarity) {
                    recommendedWithWeight.put(travelId, similarity);
                }
            });
        });

        // 4. 过滤掉目标用户已经交互过的景点
        QueryWrapper<Weight> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", userId);
        List<Weight> userBehaviors = weightService.list(wrapper);
        Set<Integer> interactedNews = userBehaviors.stream()
                .map(Weight::getTravelId)
                .collect(Collectors.toSet());

        // 按相似度权重降序排序推荐景点
        List<Integer> recommendIdList = recommendedWithWeight.entrySet().stream()
                .filter(entry -> !interactedNews.contains(entry.getKey())) // 过滤已交互景点
                .sorted(Map.Entry.<Integer, Double>comparingByValue().reversed()) // 按相似度权重降序排序
                .limit(num)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

        // 如果推荐结果为空，则返回空数组
        if (recommendIdList.isEmpty()) {
            return Collections.emptyList();
        }

        System.out.println("\n3. 推荐结果（原格式）==========");
        // 遍历推荐结果（原格式）
        for (Integer id : recommendIdList) {
            System.out.println("推荐的景点ID----> " + id);
        }

        System.out.println("\n4. 推荐结果矩阵==========");
        // 构建最终推荐结果的评分矩阵
        Map<Integer, Map<Integer, Integer>> finalMatrix = new HashMap<>();
        // 添加目标用户的评分
        finalMatrix.put(userId, userItemMatrix.getOrDefault(userId, new HashMap<>()));
        // 添加推荐项目的其他用户评分
        for (Integer itemId : recommendIdList) {
            for (Map.Entry<Integer, Map<Integer, Integer>> entry : userItemMatrix.entrySet()) {
                if (!entry.getKey().equals(userId) && entry.getValue().containsKey(itemId)) {
                    finalMatrix.putIfAbsent(entry.getKey(), new HashMap<>());
                    finalMatrix.get(entry.getKey()).put(itemId, entry.getValue().get(itemId));
                }
            }
        }
        // 打印最终推荐矩阵
        printUserItemMatrix(finalMatrix);

        // 获取推荐的景点信息，保持推荐顺序
        QueryWrapper<Travel> travelQueryWrapper = new QueryWrapper<>();
        travelQueryWrapper.in("id", recommendIdList);
        List<Travel> allRecommendList = this.list(travelQueryWrapper);
        
        // 创建ID到Travel对象的映射
        Map<Integer, Travel> travelMap = allRecommendList.stream()
                .collect(Collectors.toMap(Travel::getId, travel -> travel));
        
        // 按照推荐顺序重新排列景点
        List<Travel> orderedRecommendList = recommendIdList.stream()
                .map(travelMap::get)
                .filter(Objects::nonNull) // 过滤掉可能不存在的景点
                .collect(Collectors.toList());
        
        return convertVO(orderedRecommendList);
    }

    /**
     * 打印用户-物品评分矩阵
     * 将用户对物品的评分数据以矩阵的形式在控制台展示
     * @param userItemMatrix 用户-物品评分矩阵
     */
    private void printUserItemMatrix(Map<Integer, Map<Integer, Integer>> userItemMatrix) {
        if (userItemMatrix == null || userItemMatrix.isEmpty()) {
            System.out.println("评分矩阵为空");
            return;
        }

        // 获取所有用户ID和物品ID
        Set<Integer> userIds = userItemMatrix.keySet();
        Set<Integer> itemIds = new HashSet<>();
        userItemMatrix.values().forEach(map -> itemIds.addAll(map.keySet()));

        // 按ID排序
        List<Integer> sortedUserIds = new ArrayList<>(userIds);
        List<Integer> sortedItemIds = new ArrayList<>(itemIds);
        Collections.sort(sortedUserIds);
        Collections.sort(sortedItemIds);

        // 打印表头
        System.out.println("\n用户-物品评分矩阵:");
        System.out.print("User/Item      |");
        for (Integer itemId : sortedItemIds) {
            System.out.printf("%13d |", itemId);
        }

        // 创建分隔线
        StringBuilder separator = new StringBuilder();
        for (int i = 0; i < 15 + sortedItemIds.size() * 16; i++) {
            separator.append("-");
        }

        System.out.println("\n" + separator);

        // 打印评分矩阵主体
        for (Integer userId : sortedUserIds) {
            System.out.printf("%13d |", userId);
            Map<Integer, Integer> userRatings = userItemMatrix.get(userId);

            for (Integer itemId : sortedItemIds) {
                Integer rating = userRatings.get(itemId);
                if (rating == null) {
                    System.out.print("              |");
                } else {
                    System.out.printf(" %12d |", rating);
                }
            }
            System.out.println();
        }
        System.out.println(separator + "\n");
    }

    /**
     * 计算用户相似度矩阵
     * @return 用户相似度矩阵Map<用户ID, Map<相似用户ID, 相似度>>
     */
    public Map<Integer, Map<Integer, Double>> calculateUserSimilarityMatrix() {
        Map<Integer, Map<Integer, Double>> similarityMatrix = new HashMap<>();

        // 1. 构建用户-物品评分矩阵
        Map<Integer, Map<Integer, Integer>> userItemMatrix = new HashMap<>();

        // 获取所有用户行为数据
        List<Weight> allBehaviors = weightService.getAll();

        // 构建评分矩阵
        for (Weight behavior : allBehaviors) {
            userItemMatrix.computeIfAbsent(behavior.getUserId(), k -> new HashMap<>());

            // 根据不同行为类型赋予不同分值
            int score = 0;
            switch (behavior.getBehavior()) {
                case "view":
                    score = 1;  // 浏览
                    break;
                case "like":
                    score = 2;  // 喜欢
                    break;
                case "collect":
                    score = 3;  // 收藏
                    break;
                default:
                    score = behavior.getScore(); // 使用默认评分
            }

            userItemMatrix.get(behavior.getUserId()).put(behavior.getTravelId(), score);
        }

        // 2. 计算用户之间的相似度
        List<Integer> userIds = new ArrayList<>(userItemMatrix.keySet());

        for (int i = 0; i < userIds.size(); i++) {
            Integer user1 = userIds.get(i);
            similarityMatrix.putIfAbsent(user1, new HashMap<>());

            for (int j = i + 1; j < userIds.size(); j++) {
                Integer user2 = userIds.get(j);
                similarityMatrix.putIfAbsent(user2, new HashMap<>());

                // 计算两个用户的余弦相似度
                double similarity = calculateCosineSimilarity(
                        userItemMatrix.get(user1),
                        userItemMatrix.get(user2)
                );

                // 存储相似度（对称矩阵）
                similarityMatrix.get(user1).put(user2, similarity);
                similarityMatrix.get(user2).put(user1, similarity);
            }
        }

        return similarityMatrix;
    }

    /**
     * 计算两个用户的余弦相似度
     */
    private double calculateCosineSimilarity(Map<Integer, Integer> user1Ratings, Map<Integer, Integer> user2Ratings) {
        if (user1Ratings == null || user2Ratings == null) {
            return 0.0;
        }

        // 找到两个用户共同评分的物品
        Set<Integer> commonItems = new HashSet<>(user1Ratings.keySet());
        commonItems.retainAll(user2Ratings.keySet());

        if (commonItems.isEmpty()) {
            return 0.0;
        }

        // 计算分子（评分的点积）
        double dotProduct = 0.0;
        // 计算分母（评分向量的模长）
        double norm1 = 0.0;
        double norm2 = 0.0;

        for (Integer itemId : commonItems) {
            int rating1 = user1Ratings.get(itemId);
            int rating2 = user2Ratings.get(itemId);
            dotProduct += rating1 * rating2;
        }

        for (int rating : user1Ratings.values()) {
            norm1 += rating * rating;
        }

        for (int rating : user2Ratings.values()) {
            norm2 += rating * rating;
        }

        // 避免除以零
        if (norm1 == 0.0 || norm2 == 0.0) {
            return 0.0;
        }

        return dotProduct / (Math.sqrt(norm1) * Math.sqrt(norm2));
    }

    /**
     * 构建用户-物品评分矩阵
     * 根据用户行为数据构建评分矩阵，不同行为类型对应不同的评分值
     * @return 返回用户-物品评分矩阵
     */
    private Map<Integer, Map<Integer, Integer>> buildUserItemMatrix() {
        Map<Integer, Map<Integer, Integer>> userItemMatrix = new HashMap<>();

        List<Weight> allBehaviors = weightService.getAll();

        for (Weight behavior : allBehaviors) {
            userItemMatrix.computeIfAbsent(behavior.getUserId(), k -> new HashMap<>());

            int score = 0;
            switch (behavior.getBehavior()) {
                case "view":
                    score = 1;  // 浏览
                    break;
                case "like":
                    score = 2;  // 喜欢
                    break;
                case "collect":
                    score = 3;  // 收藏
                    break;
                default:
                    score = behavior.getScore();
            }

            userItemMatrix.get(behavior.getUserId()).put(behavior.getTravelId(), score);
        }

        return userItemMatrix;
    }

    @Override
    public List<TravelVO> randomTravels(Integer num) {
        List<Travel> travelList = this.getAll();
        // 如果用户未登录, 随机返回指定数量的景点
        Collections.shuffle(travelList);
        return convertVO(travelList.subList(0, num));
    }
}