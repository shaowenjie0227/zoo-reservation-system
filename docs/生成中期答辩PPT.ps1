$ErrorActionPreference = 'Stop'
$outputPath = Join-Path $PSScriptRoot '奇幻动物园预约管理平台-中期答辩.pptx'
$slides = @(
    @{ Title = '基于 Spring Boot 的奇幻动物园预约管理系统的设计与实现'; Subtitle = @('中期答辩汇报', '可补充：姓名 / 学号 / 专业班级 / 指导教师 / 日期') },
    @{ Title = '课题背景与研究目标'; Bullets = @('面向动物园线上服务场景，解决信息分散、预约效率低、互动能力弱等问题。', '构建一个集信息展示、预约购票、社区互动和后台管理于一体的平台。', '提升游客获取信息、在线预约和园区管理的效率。') },
    @{ Title = '系统总体设计'; Bullets = @('架构：前后端分离。', '前端：Vue3、Vite、Element Plus、Pinia、Vue Router。', '后端：Spring Boot、MyBatis-Plus、Sa-Token、MySQL。', '扩展能力：文件上传、富文本编辑、二维码支付模拟、定时任务、AI 问答接口。', '角色：普通用户与管理员。') },
    @{ Title = '目前已完成的核心功能'; Bullets = @('用户与权限：注册、登录、退出登录、个人信息、密码修改。', '信息展示：动物分类、动物信息、动物地点、公告、轮播图、生态保护资讯。', '预约业务：门票管理、在线预约下单、订单查询与取消、库存联动。', '社区互动：发帖、评论、点赞、收藏、举报。', '内容扩展：旅游景点、旅游攻略、攻略分类与发布。') },
    @{ Title = '前台已完成页面'; Bullets = @('首页：轮播图、热门动物、热门帖子。', '动物与地点：列表页、详情页、地图展示。', '社区：帖子列表、帖子详情、发帖、我的帖子、我的评论、我的点赞、我的收藏。', '攻略：攻略列表、详情、发布、我的攻略。', '订单：门票预约、二维码支付模拟、我的订单。') },
    @{ Title = '后台已完成管理功能'; Bullets = @('用户管理、公告管理、轮播图管理。', '动物分类、动物信息、动物地点管理。', '论坛帖子、帖子分类、评论、点赞、收藏、举报管理。', '生态保护资讯、旅游景点、旅游攻略、攻略分类管理。', '门票管理、门票订单管理。') },
    @{ Title = '关键实现成果'; Bullets = @('已建立较完整的数据实体、控制器与业务服务。', '已实现订单编号生成、库存扣减与取消返还。', '已实现订单超时处理定时任务，具备进一步完善基础。', '已集成 AI 问答前后端接口和浮动入口组件。', '已支持图文内容发布与文件上传。') },
    @{ Title = '待完成的功能'; Bullets = @('完善数据统计分析模块，并接入后台首页图表展示。', '继续优化前后端联调、异常处理和页面细节。', '补充接口测试、功能测试和关键流程验证。', '完成 AI 服务真实配置与效果调优。', '同步推进论文撰写、截图整理和答辩材料收尾。') },
    @{ Title = '存在的困难与问题'; Bullets = @('业务模块较多，系统联调工作量较大。', '部分前后端字段命名与展示逻辑仍需统一。', '统计分析模块需要整合多业务表数据，设计复杂度较高。', 'AI 功能依赖外部接口与有效 API Key。', '当前测试覆盖率不足，后续仍需补充。') },
    @{ Title = '目前完成情况评估'; Bullets = @('系统主体框架和主要业务功能已经搭建完成。', '已完成用户、动物、地点、门票、订单、社区、资讯、攻略等主要模块。', '未完全完成部分主要集中在统计分析、测试完善和细节优化。', '结合路由、接口和业务实现情况，当前整体完成度约为 75% - 80%。') },
    @{ Title = '下一阶段计划'; Bullets = @('完成统计分析模块与后台首页整合。', '完成剩余模块联调与异常问题修复。', '优化界面细节与移动端适配。', '完成 AI 配置、测试补充和论文收尾。', '准备最终答辩演示环境与材料。') },
    @{ Title = '阶段总结'; Bullets = @('目前系统主要功能已基本成型，已具备中期检查与阶段演示条件。', '后续重点是统计分析、系统优化、测试完善和论文收尾。', '请各位老师批评指正。') }
)
$ppLayoutTitle = 1
$ppLayoutText = 2
$msoTrue = -1
$ppt = $null
$presentation = $null
try {
    $ppt = New-Object -ComObject PowerPoint.Application
    $ppt.Visible = $msoTrue
    $presentation = $ppt.Presentations.Add()
    foreach ($slideData in $slides) {
        if ($slideData.ContainsKey('Subtitle')) {
            $slide = $presentation.Slides.Add($presentation.Slides.Count + 1, $ppLayoutTitle)
            $slide.Shapes.Title.TextFrame.TextRange.Text = $slideData.Title
            $slide.Shapes.Item(2).TextFrame.TextRange.Text = ($slideData.Subtitle -join "`r`n")
        } else {
            $slide = $presentation.Slides.Add($presentation.Slides.Count + 1, $ppLayoutText)
            $slide.Shapes.Title.TextFrame.TextRange.Text = $slideData.Title
            $body = $slide.Shapes.Item(2).TextFrame.TextRange
            $body.Text = ($slideData.Bullets -join "`r`n")
            for ($i = 1; $i -le $slideData.Bullets.Count; $i++) {
                $body.Paragraphs($i).ParagraphFormat.Bullet.Visible = $msoTrue
            }
        }
    }
    $presentation.SaveAs($outputPath)
    $presentation.Close()
    $ppt.Quit()
    Write-Output $outputPath
}
catch {
    if ($presentation -ne $null) {
        try { $presentation.Close() } catch {}
    }
    if ($ppt -ne $null) {
        try { $ppt.Quit() } catch {}
    }
    throw
}
