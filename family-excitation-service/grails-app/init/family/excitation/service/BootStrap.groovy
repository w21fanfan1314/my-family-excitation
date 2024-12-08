package family.excitation.service

import family.excitation.service.train.Question
import family.excitation.service.train.QuestionOption
import family.excitation.service.train.QuestionType
import family.excitation.service.train.Train
import family.excitation.service.train.TrainLevel
import family.excitation.service.train.TrainService
import family.excitation.service.train.Transcript
import family.excitation.service.train.UserAnswer
import grails.converters.JSON
import org.apache.commons.lang3.RandomUtils

class BootStrap {
    UserService userService
    CurrencyService currencyService
    DisciplineService disciplineService
    CommodityCategoryService commodityCategoryService
    CommodityService commodityService
    UserRecordService userRecordService
    TrainService trainService
    TestPaperTrackService testPaperTrackService
    LotteryService lotteryService
    VideoItemService videoItemService
    AppConfigService appConfigService

    def init = { servletContext ->
        // 初始化配置
        def appConfigs = AppConfig.list()
        if (appConfigs.empty) {
            appConfigService.save(new AppConfig())
        } else {
            AppConfig.instance = appConfigs.last()
        }
        environments {
            development {
                // 创建一个管理员
                userService.save(new User(name: 'admin', userName: 'admin', password: 'asdf.1234', role: UserRole.ADMIN, position: UserPosition.PARENT, lotteryChance: 100))
                userService.save(new User(name: '刘泯铄', userName: 'marvin', password: '123456', role: UserRole.USER, position: UserPosition.CHILD))
                def martin = userService.save(new User(name: '王泯泽', userName: 'martin', password: '123456', role: UserRole.USER, position: UserPosition.CHILD, birthday: new Date(115, 6, 10)))


                def rmb = currencyService.save(new Currency(name: '人民币', symbol: '¥'))
                def zyb = currencyService.save(new Currency(name: '卓越币', symbol: '¥', rate: 1000))

                userRecordService.save(new UserRecord(user: martin, recordType: UserRecordType.RECHARGE, amount: 10000, currency: rmb))
                userRecordService.save(new UserRecord(user: martin, recordType: UserRecordType.RECHARGE, amount: 100000000, currency: zyb))
                for( int i = 0; i < 100; i ++) {
                    userRecordService.save(new UserRecord(user: martin, recordType: UserRecordType.RECHARGE, amount: RandomUtils.nextInt(1, 10), currency: rmb))
                }

                AppConfig.instance?.currency = rmb
                appConfigService.save(AppConfig.instance)

                disciplineService.save(new Discipline(name: '数学'))
                disciplineService.save(new Discipline(name: '语文'))
                disciplineService.save(new Discipline(name: '英语'))

                commodityCategoryService.save(new CommodityCategory(name: '学习用品', commodities: [
                        new Commodity(name: '得力中性笔S08按动笔学生专用考试红笔碳素黑色按压式速干水笔蓝黑笔墨蓝护士0.5教师红色水性签字笔芯圆珠笔',
                                price: 4.5, imageUrl: 'https://gw.alicdn.com/imgextra/i4/3159614613/O1CN01mgHaBo1jwo08YHkUT_!!3159614613.jpg',
                                referenceUrl: 'https://detail.tmall.com/item.htm?abbucket=16&id=842945746597&ns=1&pisk=fdNsLROiaoFUVzXNFEQEFbJgCBGjHo1yfEgYrrdwkfhtDJaucc7cIfyIckEIXC7GIxnbjfH0QmoZcja0VwSPa_zgSjcN4g5zCvMEYbv96VKqvv3mIM7eY_zgSmY6Daydan9BYbv9HmEx9v3ryj3tDdndRqgSDjntHHpKyDhxDonO9H3m5qdtBjHdRV0-kFnxWH3KP4T9XohARyhtat3XCca1-q3PDUxvsomBMQToAAIYrKRp9WgtCS3tA2uUOVMsVvcijYN7yPFolSbJhmauBlu3c_sI95Z_6vGfGBcaPJEsdRsXOv2LrSHQKg9urPE_eYFOOwhYBoGtASbDiceakSkb6gLqX5qaOYVDfMo4UyPtduIeOoDbB8hTGg1d4BRrVPoyheMkG2iPRw9Dnw-4BbxodorrB20_Sw_BfxDt-2GARw9cfA3n5x_CRhwC.&priceTId=213e38f317306042919498832e5f1f&skuId=5792361639690&spm=a21n57.1.item.146.7c4b523cNqob5m&utparam=%7B%22aplus_abtest%22%3A%2268cc35259bfd25812abf1b0d3d9baff9%22%7D&xxc=taobaoSearch'),
                        new Commodity(name: '得力学科科目分类文件袋小学生拉链式试卷收纳袋透明网纱双层大容量书本课本分科书袋装卷子袋子作业资料A4', price: 10.0,
                                imageUrl: 'https://img.alicdn.com/imgextra/i3/2579937287/O1CN01TJRV7423hV3SqUNCx_!!2579937287.jpg'),
                        new Commodity(name: '可擦钢笔圆珠笔中性笔擦神器磨砂橡皮擦铅笔擦不留痕学生两用考试', price: 9.9,
                                imageUrl: 'https://img.alicdn.com/imgextra/i4/2061425410/O1CN01OggSR61pppiGRtsff_!!2061425410.jpg')
                ]))
                commodityCategoryService.save(new CommodityCategory(name: '玩具', commodities: [
                        new Commodity(name: '益智隐藏积木拼图3到6岁以上大脑空间思维训练双人对战女男孩玩具', price: 4.3,
                                imageUrl: 'https://img.alicdn.com/imgextra/i2/2217541211398/O1CN01F9C0Wd1MCKfX4DFSm_!!2217541211398.jpg',
                                referenceUrl: 'https://detail.tmall.com/item.htm?abbucket=16&id=812114594910&ns=1&pisk=f8eoL30YEqTsy5Ammsk5tHPnMl1vm3MINypKJv3FgquXeXudNyc3JrGUeYE8o2zT-De8pJVXKlZQegMdVYZSdv7OWOF3VuMIEMmQ48A40DE2aBRyrukW4v7OW9Av0b_-LzFUWxp2gcgq4Dor8iDqXc8rLXoz0imEvUReUyS00qiB4UkeajuqvDoELplrgxoKbQ-r8Qk40qiELQl9oN0iLx2VPU0dseN6-5ioZ2vKqpmLKdhI7S32LNrEqFga4qveK4hXvbEU8wvKQ5a0KfzCKKM8jSkqqSjeg4lgaznb-Ovrz-qzU4ePkL3zFoPTprSeL2V4_fmg3g5Za5a8_j2ArKgagoqKiSsBw2FsYyG_pN9Szkr_C5HGKHm0sokc4aO2_IaBdmSL3BOIamimWb9OXHepIZSCmiA1DbojVFIcmB6oami08ijD1_lrc0NV.&priceTId=213e38b017306041452037291e048c&skuId=5509269813628&spm=a21n57.1.item.98.7c4b523cNqob5m&utparam=%7B%22aplus_abtest%22%3A%228dc10a5939b59784ad063c033d6c76a4%22%7D&xxc=taobaoSearch'),

                        new Commodity(name: '坦克300儿童电动车四轮越野汽车遥控小孩宝宝玩具车可坐大人童车', price: 590.3,
                                imageUrl: 'https://img.alicdn.com/imgextra/i4/248453511/O1CN015Uf0hJ1bo5N1AHtbf_!!248453511.jpg',
                                referenceUrl: 'https://item.taobao.com/item.htm?id=652736419333&ns=1&pisk=fXh-LOqrjEQJqhQAH990K7NODR8DyX3yq0u1tkqldmnxRofHz7vU9DExYu2uNufL92nExD3ezyZIADEnEKAiz4PUOh0psC0yakH3F0CQOsTQ7PaWwIvDP4PUOHQcOdAtzcZgsNU7RrTY-yX5FDw5Grazcy67ODZbhPaGVWiQOEEbrPrCPJZ7GqaLc967d_ZfhPzdP_Z7VE3b0yGOX1ZXPk1KLEUV5gYyROBtMrtUR4LdGyl_y8EsPbQGSez8elg7chRpj5yK28hDwGz-hVnuyDRNa7wKFANS9hd8GVkmVrnBX9EjBblLIbKOQuGzYcFSHn18O7Ms6WNpBazmwqGapb-RGunqlAVr1n5Kfq0ia-lpXiZEEPPbyxLChuwC4xci6uTVjlUhFELAL9y7uL7PFaLAHQKuklYRf9WUnZzYjEpPL9ySyrEMyIXFL-cO.&priceTId=213e38b017306041452037291e048c&skuId=5632737728967&spm=a21n57.1.item.103.7c4b523cNqob5m&utparam=%7B%22aplus_abtest%22%3A%22825fc110a922ed42fc1f6cddf9b54671%22%7D&xxc=ad_ztc'),
                ]))
                commodityCategoryService.save(new CommodityCategory(name: '图书'))
                commodityCategoryService.save(new CommodityCategory(name: '零食'))
                commodityCategoryService.save(new CommodityCategory(name: '试卷'))

                trainService.save(new Train(name: '英语大闯关', levels: [
                        new TrainLevel(level: 1, award: 100, awardMaxCount: 5, questions: [
                                new Question(content: 'What is the name of this country?', answer: 'China', type: QuestionType.SINGLE, options: [
                                        new QuestionOption(option: 'China', isRight: true),
                                        new QuestionOption(option: 'India'),
                                        new QuestionOption(option: 'USA'),
                                        new QuestionOption(option: 'UK')
                                ], meidaDataList: [
                                        new MediaData(type: MediaType.IMAGE, url: 'https://img.alicdn.com/imgextra/i2/2217541211398/O1CN01F9C0Wd1MCKfX4DFSm_!!2217541211398.jpg'),
                                        new MediaData(type: MediaType.IMAGE, url: 'https://gw.alicdn.com/imgextra/i4/3159614613/O1CN01mgHaBo1jwo08YHkUT_!!3159614613.jpg')
                                ]),
                                new Question(content: 'Who is the current president of China?', answer: 'Xi Jinping', type: QuestionType.SINGLE, options: [
                                        new QuestionOption(option: 'Xi Jinping'),
                                        new QuestionOption(option: 'Putin', isRight: true),
                                ]),
                                new Question(content: 'meaning of the word "china"?', type: QuestionType.MULTIPLE, options: [
                                        new QuestionOption(option: 'a country'),
                                        new QuestionOption(option: 'a city'),
                                        new QuestionOption(option: 'a language'),
                                        new QuestionOption(option: 'a country', isRight: true)
                                ]),
                                new Question(content: 'biggest city in China?', type: QuestionType.ANSWER, answer: 'Beijing'),
                                new Question(content: 'judge the following sentence: China is a big country.', type: QuestionType.JUDGE, options: [
                                        new QuestionOption(option: '对'),
                                        new QuestionOption(option: '错', isRight: true)
                                ], meidaDataList: [
                                        new MediaData(type: MediaType.VIDEO, url: 'http://mirror.aarnet.edu.au/pub/TED-talks/911Mothers_2010W-480p.mp4'),
                                ])
                        ]),
                        new TrainLevel(level: 2, award: 200, awardMaxCount: 2, currency: rmb, questions: [
                                new Question(content: 'just a test', type: QuestionType.SINGLE, options: [
                                        new QuestionOption(option: 'just a test1', isRight: true),
                                        new QuestionOption(option: 'just a test')
                                ]),
                                new Question(content: 'just a test2', type: QuestionType.SINGLE, options: [
                                        new QuestionOption(option: 'just a test1', isRight: true),
                                        new QuestionOption(option: 'just a test')
                                ]),
                                new Question(content: 'just a test3', type: QuestionType.SINGLE, options: [
                                        new QuestionOption(option: 'just a test1', isRight: true),
                                        new QuestionOption(option: 'just a test')
                                ]),
                                new Question(content: 'just a test4', type: QuestionType.SINGLE, options: [
                                        new QuestionOption(option: 'just a test1', isRight: true),
                                        new QuestionOption(option: 'just a test')
                                ])
                        ]),
                        new TrainLevel(level: 3, award: 300, awardMaxCount: 5, questions: []),
                        new TrainLevel(level: 4, award: 400, awardMaxCount: 5, questions: []),
                        new TrainLevel(level: 5, award: 500, awardMaxCount: 5, questions: []),
                        new TrainLevel(level: 6, award: 600, awardMaxCount: 5, questions: []),
                        new TrainLevel(level: 7, award: 700, awardMaxCount: 5, questions: []),
                        new TrainLevel(level: 8, award: 800, awardMaxCount: 5, questions: []),
                        new TrainLevel(level: 9, award: 900, awardMaxCount: 5, questions: []),
                        new TrainLevel(level: 10, award: 1000, awardMaxCount: 5, questions: []),
                ], category: '英语', image: 'http://47.120.23.110/res/train_en.jpg'))
                trainService.save(new Train(name: '数学大闯关', category: '数学'))
                trainService.save(new Train(name: '语文大闯关', category: '语文'))

                testPaperTrackService.save(new TestPaperTrack(user: martin, currency: rmb, award: 10))

                lotteryService.save(new Lottery(name: "现金10元", image: 'https://png.pngtree.com/element_our/20190529/ourmid/pngtree-round-cartoon-gold-coin-image_1194649.jpg', type: LotteryType.AMOUNT, amount: 10, currency: rmb))
                lotteryService.save(new Lottery(name: "现金2元", image: 'https://png.pngtree.com/element_our/20190529/ourmid/pngtree-round-cartoon-gold-coin-image_1194649.jpg', type: LotteryType.AMOUNT, amount: 2, currency: rmb))
                lotteryService.save(new Lottery(name: "现金5元", image: 'https://png.pngtree.com/element_our/20190529/ourmid/pngtree-round-cartoon-gold-coin-image_1194649.jpg', type: LotteryType.AMOUNT, amount: 5, currency: rmb))
                lotteryService.save(new Lottery(name: "遥控车", image: 'https://bpic.588ku.com/element_pic/24/01/23/d4341d55b19a3a700dfc1b27dc2fbb84.png!/fw/350/quality/99/unsharp/true/compress/true', type: LotteryType.THING))
                lotteryService.save(new Lottery(name: "精美小礼品", image: 'https://i-1.lanrentuku.com/2020/11/3/a7999f21-c054-4439-93ea-46221bbd5bfb.png', type: LotteryType.THING))
                lotteryService.save(new Lottery(name: "铅笔", image: 'https://img95.699pic.com/xsj/14/lh/rp.jpg!/fw/700/watermark/url/L3hzai93YXRlcl9kZXRhaWwyLnBuZw/align/southeast', type: LotteryType.THING))

                videoItemService.save(new VideoItem(category: '搞笑', name: '你投的币最后都去哪儿了？', url: 'https://cn-gdfs-ct-01-05.bilivideo.com/upgcxcode/56/97/26790199756/26790199756-1-16.mp4?e=ig8euxZM2rNcNbRVhwdVhwdlhWdVhwdVhoNvNC8BqJIzNbfq9rVEuxTEnE8L5F6VnEsSTx0vkX8fqJeYTj_lta53NCM=&uipk=5&nbs=1&deadline=1733573953&gen=playurlv2&os=bcache&oi=0&trid=0000f2d42602597e482c9bf2628473df28fch&mid=0&platform=html5&og=cos&upsig=2ec3c1c2a01e9f1ac95a6dd3e0c0d505&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform,og&cdnid=60905&bvc=vod&nettype=0&f=h_0_0&bw=48093&logo=80000000'))
                videoItemService.save(new VideoItem(category: '直播', name: '顶级阳谋，全体吃屎！', url: 'http://60.10.167.88:808/hls/57/index.m3u8'))
                videoItemService.save(new VideoItem(category: "搞笑", name: '帮我办个医院VIP ｜巴卫', url: 'https://cn-gdfs-ct-01-15.bilivideo.com/upgcxcode/88/54/26775195488/26775195488-1-16.mp4?e=ig8euxZM2rNcNbRVhwdVhwdlhWdVhwdVhoNvNC8BqJIzNbfq9rVEuxTEnE8L5F6VnEsSTx0vkX8fqJeYTj_lta53NCM=&uipk=5&nbs=1&deadline=1733575208&gen=playurlv2&os=bcache&oi=0&trid=000089e051199f494e759d0d156738625a93h&mid=0&platform=html5&og=hw&upsig=e72d0aa32e11699953b125be860395ed&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,mid,platform,og&cdnid=60915&bvc=vod&nettype=0&f=h_0_0&bw=56360&logo=80000000'))
            }
            production {
                if (!User.findByUserName("admin")) {
                    userService.save(new User(name: 'admin', userName: 'admin', password: 'asdf.1234', role: UserRole.ADMIN, position: UserPosition.PARENT))
                }
                if (!User.findByUserName("matt")) {
                    userService.save(new User(name: '刘洪宝', userName: 'matt', password: 'asdf.1234', role: UserRole.USER, position: UserPosition.PARENT))
                }
            }
        }


        JSON.registerObjectMarshaller(Score) {
            return [
                    id: it.id,
                    score: it.score,
                    level: it.level,
                    award: it.award,
                    awdCurrency: it.awardCurrency,
                    user: it.user,
                    discipline: it.discipline,
                    dateCreated: it.dateCreated,
                    lastUpdated: it.lastUpdated
            ]
        }

        JSON.registerObjectMarshaller(User) {
            return [
                    id: it.id,
                    name: it.name,
                    userName: it.userName,
                    password: it.password,
                    birthday: it.birthday,
                    avatar: it.avatar,
                    gender: it.gender,
                    role: it.role,
                    position: it.position,
                    dateCreated: it.dateCreated,
                    lastUpdated: it.lastUpdated,
                    lotteryChance: it.lotteryChance
            ]
        }
        JSON.registerObjectMarshaller(Discipline) {
            return [
                    id: it.id,
                    name: it.name,
                    dateCreated: it.dateCreated,
                    lastUpdated: it.lastUpdated
            ]
        }
        JSON.registerObjectMarshaller(Currency) {
            return [
                    id: it.id,
                    name: it.name,
                    symbol: it.symbol,
                    rate: it.rate,
                    interest: it.interest,
                    dateCreated: it.dateCreated,
                    lastUpdated: it.lastUpdated
            ]
        }
        JSON.registerObjectMarshaller(Commodity) {
            return [
                    id: it.id,
                    name: it.name,
                    price: it.price,
                    imageUrl: it.imageUrl,
                    referenceUrl: it.referenceUrl,
                    category: it.category,
                    specification: it.specification,
                    dateCreated: it.dateCreated,
                    lastUpdated: it.lastUpdated
            ]
        }
        JSON.registerObjectMarshaller(CommodityCategory) {
            return [
                    id: it.id,
                    name: it.name,
                    dateCreated: it.dateCreated,
                    lastUpdated: it.lastUpdated
            ]
        }
        JSON.registerObjectMarshaller(Login) {
            return [
                    id: it.id,
                    type: it.type,
                    token: it.token,
                    user: it.user,
                    dateCreated: it.dateCreated,
                    lastUpdated: it.lastUpdated
            ]
        }
        JSON.registerObjectMarshaller(UserRecord) {
            return [
                    id: it.id,
                    content: it.content,
                    amount: it.amount,
                    recordType: it.recordType,
                    user: it.user,
                    currency: it.currency,
                    dateCreated: it.dateCreated,
                    lastUpdated: it.lastUpdated
            ]
        }
        JSON.registerObjectMarshaller(Order) {
            return [
                    id: it.id,
                    orderNo: it.orderNo,
                    totalPrice: it.totalPrice,
                    status: it.status,
                    payments: it.payments,
                    items: it.items,
                    dateCreated: it.dateCreated,
                    lastUpdated: it.lastUpdated
            ]
        }
        JSON.registerObjectMarshaller(OrderItem) {
            return [
                    id: it.id,
                    name: it.name,
                    imageUrl: it.imageUrl,
                    price: it.price,
                    buyCount: it.buyCount,
                    specification: it.specification,
                    commodity: it.commodity,
                    dateCreated: it.dateCreated,
                    lastUpdated: it.lastUpdated
            ]
        }
        JSON.registerObjectMarshaller(OrderPayment) {
            return [
                    id: it.id,
                    amount: it.amount,
                    currency: it.currency,
                    dateCreated: it.dateCreated,
                    lastUpdated: it.lastUpdated
            ]
        }
        JSON.registerObjectMarshaller(ReleaseInformation) {
            return [
                    id: it.id,
                    title: it.title,
                    content: it.content,
                    dateCreated: it.dateCreated,
                    lastUpdated: it.lastUpdated,
                    mediaDataList: it.mediaDataList,
                    thumbnail: it.thumbnail,
                    user: it.user,
                    category: it.category
            ]
        }
        JSON.registerObjectMarshaller(MediaData) {
            return [
                    id: it.id,
                    name: it.name,
                    url: it.url,
                    type: it.type,
                    contentType: it.contentType,
                    dateCreated: it.dateCreated,
                    lastUpdated: it.lastUpdated
            ]
        }
        JSON.registerObjectMarshaller(QuestionOption) {
            return [
                    id: it.id,
                    option: it.option,
                    isRight: it.isRight,
                    dateCreated: it.dateCreated,
                    lastUpdated: it.lastUpdated
            ]
        }
        JSON.registerObjectMarshaller(Question) {
            return [
                    id: it.id,
                    content: it.content,
                    answer: it.answer,
                    type: it.type,
                    dateCreated: it.dateCreated,
                    lastUpdated: it.lastUpdated,
                    meidaDataList: it.meidaDataList,
                    options: it.options,
                    rightOption: it.rightOption
            ]
        }
        JSON.registerObjectMarshaller(UserAnswer) {
            return [
                    id: it.id,
                    answer: it.answer,
                    dateCreated: it.dateCreated,
                    lastUpdated: it.lastUpdated,
                    question: it.question,
                    userSelectedOptions: it.userSelectedOptions,
                    correct: it.correct
            ]
        }
        JSON.registerObjectMarshaller(TrainLevel) {
            return [
                    id: it.id,
                    level: it.level,
                    dateCreated: it.dateCreated,
                    lastUpdated: it.lastUpdated,
                    questionCount: it.questionCount,
                    award: it.award,
                    awardMaxCount: it.awardMaxCount,
                    awardType: it.awardType,
                    currency: it.currency
            ]
        }
        JSON.registerObjectMarshaller(Train) {
            return [
                    id: it.id,
                    name: it.name,
                    category: it.category,
                    description: it.description,
                    image: it.image,
                    dateCreated: it.dateCreated,
                    lastUpdated: it.lastUpdated,
                    levels: it.levels
            ]
        }

        JSON.registerObjectMarshaller(Transcript) {
            return [
                    id: it.id,
                    score: it.score,
                    dateCreated: it.dateCreated,
                    lastUpdated: it.lastUpdated,
                    level: it.level,
                    answers: it.answers,
                    wrongs: it.wrongs,
                    starCount: it.starCount,
                    hasAward: it.hasAward
            ]
        }
        JSON.registerObjectMarshaller(TestPaperTrack) {
            return [
                    id             : it.id,
                    user           : it.user,
                    name           : it.name,
                    status         : it.status,
                    totalScore     : it.totalScore,
                    qualifiedScore : it.qualifiedScore,
                    score          : it.score,
                    startTime      : it.startTime,
                    endTime        : it.endTime,
                    award          : it.award,
                    actualAward    : it.actualAward,
                    currency       : it.currency,
                    answerLimitTime: it.answerLimitTime,
                    dateCreated    : it.dateCreated,
                    lastUpdated    : it.lastUpdated
            ]
        }
        JSON.registerObjectMarshaller(Lottery) {
            return [
                    id             : it.id,
                    name           : it.name,
                    image          : it.image,
                    amount         : it.amount,
                    currency       : it.currency,
                    type           : it.type,
                    dateCreated    : it.dateCreated,
                    lastUpdated    : it.lastUpdated
            ]
        }

        JSON.registerObjectMarshaller(LotteryRecords) {
            return [
                    id         : it.id,
                    user       : it.user,
                    lottery    : it.lottery,
                    dateCreated: it.dateCreated,
                    lastUpdated: it.lastUpdated,
                    exchanged  : it.exchanged
            ]
        }
    }
    def destroy = {
    }
}
