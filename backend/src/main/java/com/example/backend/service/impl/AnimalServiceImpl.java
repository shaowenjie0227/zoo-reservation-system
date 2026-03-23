package com.example.backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.backend.entity.*;
import com.example.backend.entity.request.animal.AnimalRequest;
import com.example.backend.entity.vo.animal.AnimalVO;
import com.example.backend.entity.vo.user.UserLoginVO;
import com.example.backend.mapper.AnimalMapper;
import com.example.backend.service.IAnimalService;
import com.example.backend.service.IAnimalTypeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.backend.service.IAnimalWeightService;
import com.example.backend.service.IUserService;
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
 * 动物信息表 服务实现类
 * </p>
 *
 * @author
 */
@Service
public class AnimalServiceImpl extends ServiceImpl<AnimalMapper, Animal> implements IAnimalService {
    @Resource
    private AnimalMapper animalMapper;
    @Resource
    private IAnimalTypeService animalTypeService;
    @Resource
    private IUserService userService;
    @Resource
    private IAnimalWeightService animalWeightService;


    /**
     * 新增
     *
     * @param request
     * @return
     */
    @Override
    public Integer add(Animal request) {
        Animal saveData = new Animal();
        BeanUtils.copyProperties(request, saveData);
        animalMapper.insert(saveData);
        return saveData.getId();
    }

    /**
     * 批量新增
     *
     * @param request
     * @return
     */
    @Override
    public Boolean batchAdd(List<Animal> request) {
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
     * @param id
     * @param request
     * @return
     */
    @Override
    public Boolean edit(Integer id, AnimalRequest request) {
        Animal animal = new Animal();
        BeanUtils.copyProperties(request, animal);
        if (request.getTagList() != null) {
            animal.setTagsList(request.getTagList());
        }
        animal.setId(id);
        return updateById(animal);
    }

    /**
     * 分页查询
     *
     * @return
     */
    @Override
    public PageResult<List<AnimalVO>> queryPage(Integer pageNum, Integer pageSize, String name, String typeId) {
        QueryWrapper<Animal> queryWrapper = new QueryWrapper<>();
        if (CharSequenceUtil.isNotBlank(name)) {
            queryWrapper.like("name", name);
        }
        if (CharSequenceUtil.isNotBlank(typeId)) {
            queryWrapper.eq("type_id", typeId);
        }
        // ID 降序
        queryWrapper.orderByDesc("id");
        Page<Animal> page = page(new Page<>(pageNum, pageSize), queryWrapper);
        // 返回结果
        List<AnimalVO> animalVOS = convertVO(page.getRecords());
        return new PageResult<>(animalVOS, page.getCurrent(), page.getSize(), page.getTotal());
    }

    /**
     * 查询全部
     *
     * @return
     */
    @Override
    public List<Animal> getAll() {
        return list();
    }

    /**
     * 根据id查询
     *
     * @param id
     * @return
     */
    @Override
    public AnimalVO getByIdDetail(Integer id) {
        // 转换VO
        AnimalVO animalVO = new AnimalVO();
        List<AnimalVO> animalVOS = convertVO(Collections.singletonList(getById(id)));
        if (!animalVOS.isEmpty()) {
            animalVO = animalVOS.get(0);
        }
        // 浏览量 + 1
        if (animalVO == null) {
            throw new BusinessException(CodeEnum.PARAMS_ERROR, "动物不存在");
        }
        if (animalVO.getViewCount() == null) {
            animalVO.setViewCount(0);
        }
        animalVO.setViewCount(animalVO.getViewCount() + 1);
        updateById(animalVO);

        // 添加浏览权重记录（1分）
        try {
            UserLoginVO currentUser = userService.getCurrentUserInfo();
            if (currentUser != null) {
                // 判断用户是否存在该动物的浏览记录
                AnimalWeight existingWeight = animalWeightService.getOne(new LambdaQueryWrapper<AnimalWeight>()
                        .eq(AnimalWeight::getUserId, currentUser.getId())
                        .eq(AnimalWeight::getAnimalId, animalVO.getId())
                        .eq(AnimalWeight::getBehavior, "view"));

                if (existingWeight == null) {
                    AnimalWeight weight = new AnimalWeight();
                    weight.setUserId(currentUser.getId());
                    weight.setAnimalId(animalVO.getId());
                    weight.setBehavior("view");
                    weight.setScore(1);
                    animalWeightService.add(weight);
                }
            }
        } catch (Exception e) {
            // 权重记录失败不影响主要功能，记录日志即可
            System.err.println("添加浏览权重记录失败: " + e.getMessage());
        }
        return animalVO;
    }

    @Override
    public List<AnimalVO> recommend(Integer num) {
        // 获取用户信息
        User user = userService.getLoginUserInfo();
        if (user == null) {
            // 如果用户未登录, 随机返回指定数量的动物
            return randomAnimals(num);
        }
        Integer userId = user.getId();
        // 基于协同过滤算法实现推荐
        // 1. 计算所有用户之间的相似度矩阵
        Map<Integer, Map<Integer, Double>> similarityMatrix = calculateUserSimilarityMatrix();

        // 2. 获取与目标用户相似的其他用户
        Map<Integer, Double> similarUsers = similarityMatrix.getOrDefault(userId, new HashMap<>());

        // 打印初始评分矩阵
        Map<Integer, Map<Integer, Integer>> userItemMatrix = buildUserItemMatrix();

        // 3. 收集相似用户喜欢的动物，使用LinkedHashMap保持相似度权重
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

        // 遍历取出相似用户喜欢的动物，按相似度权重收集
        sortUsers.forEach(entry -> {
            Integer similarUserId = entry.getKey();
            Double similarity = entry.getValue();
            QueryWrapper<AnimalWeight> wrapper = new QueryWrapper<>();
            wrapper.eq("user_id", similarUserId);
            List<AnimalWeight> behaviors = animalWeightService.list(wrapper);
            behaviors.forEach(b -> {
                Integer travelId = b.getAnimalId();
                // 如果动物已存在，保留更高的相似度权重
                if (!recommendedWithWeight.containsKey(travelId) ||
                        recommendedWithWeight.get(travelId) < similarity) {
                    recommendedWithWeight.put(travelId, similarity);
                }
            });
        });

        // 4. 过滤掉目标用户已经交互过的动物
        QueryWrapper<AnimalWeight> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", userId);
        List<AnimalWeight> userBehaviors = animalWeightService.list(wrapper);
        Set<Integer> interactedNews = userBehaviors.stream()
                .map(AnimalWeight::getAnimalId)
                .collect(Collectors.toSet());

        // 按相似度权重降序排序推荐动物
        List<Integer> recommendIdList = recommendedWithWeight.entrySet().stream()
                .filter(entry -> !interactedNews.contains(entry.getKey())) // 过滤已交互动物
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
            System.out.println("推荐的动物ID----> " + id);
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

        // 获取推荐的动物信息，保持推荐顺序
        QueryWrapper<Animal> animalQueryWrapper = new QueryWrapper<>();
        animalQueryWrapper.in("id", recommendIdList);
        List<Animal> allRecommendList = this.list(animalQueryWrapper);

        // 创建ID到Animal对象的映射
        Map<Integer, Animal> animalMap = allRecommendList.stream()
                .collect(Collectors.toMap(Animal::getId, animal -> animal));

        // 按照推荐顺序重新排列动物
        List<Animal> orderedRecommendList = recommendIdList.stream()
                .map(animalMap::get)
                .filter(Objects::nonNull) // 过滤掉可能不存在的动物
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
        List<AnimalWeight> allBehaviors = animalWeightService.getAll();

        // 构建评分矩阵
        for (AnimalWeight behavior : allBehaviors) {
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

            userItemMatrix.get(behavior.getUserId()).put(behavior.getAnimalId(), score);
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

        List<AnimalWeight> allBehaviors = animalWeightService.getAll();

        for (AnimalWeight behavior : allBehaviors) {
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

            userItemMatrix.get(behavior.getUserId()).put(behavior.getAnimalId(), score);
        }

        return userItemMatrix;
    }


    // 转换VO
    private List<AnimalVO> convertVO(List<Animal> animals) {
        ArrayList<AnimalVO> list = new ArrayList<>();
        for (Animal animal : animals) {
            AnimalVO animalVO = new AnimalVO();
            BeanUtils.copyProperties(animal, animalVO);
            // 获取物种信息
            if (animal.getTypeId() != null) {
                AnimalType animalType = animalTypeService.getById(animal.getTypeId());
                if (animalType != null) {
                    animalVO.setTypeName(animalType.getName());
                }
            }
            list.add(animalVO);
        }
        return list;
    }

    @Override
    public List<AnimalVO> randomAnimals(Integer num) {
        List<Animal> travelList = this.getAll();
        // 如果用户未登录, 随机返回指定数量的动物
        Collections.shuffle(travelList);
        return convertVO(travelList.subList(0, num));
    }
}