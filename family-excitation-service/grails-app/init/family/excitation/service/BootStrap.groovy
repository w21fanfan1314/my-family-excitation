package family.excitation.service

import family.excitation.service.train.Question
import family.excitation.service.train.QuestionOption
import family.excitation.service.train.QuestionType
import family.excitation.service.train.Train
import family.excitation.service.train.TrainLevel
import family.excitation.service.train.TrainService
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

    def init = { servletContext ->
        environments {
            development {
                // 创建一个管理员
                userService.save(new User(name: 'admin', userName: 'admin', password: 'asdf.1234', role: UserRole.ADMIN, position: UserPosition.PARENT))
                userService.save(new User(name: '刘泯铄', userName: 'marvin', password: '123456', role: UserRole.USER, position: UserPosition.CHILD))
                def martin = userService.save(new User(name: '王泯泽', userName: 'martin', password: '123456', role: UserRole.USER, position: UserPosition.CHILD, birthday: new Date(115, 6, 10)))


                def rmb = currencyService.save(new Currency(name: '人民币', symbol: '¥'))
                def zyb = currencyService.save(new Currency(name: '卓越币', symbol: '¥', rate: 1000))

                userRecordService.save(new UserRecord(user: martin, recordType: UserRecordType.RECHARGE, amount: 10000, currency: rmb))
                userRecordService.save(new UserRecord(user: martin, recordType: UserRecordType.RECHARGE, amount: 100000000, currency: zyb))
                for( int i = 0; i < 100; i ++) {
                    userRecordService.save(new UserRecord(user: martin, recordType: UserRecordType.RECHARGE, amount: RandomUtils.nextInt(1, 10), currency: rmb))
                }

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
                        new TrainLevel(level: 2, award: 200, awardMaxCount: 5, questions: []),
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
                    lastUpdated: it.lastUpdated
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
                    options: it.options
            ]
        }
        JSON.registerObjectMarshaller(UserAnswer) {
            return [
                    id: it.id,
                    answer: it.answer,
                    dateCreated: it.dateCreated,
                    lastUpdated: it.lastUpdated,
                    question: it.question,
                    options: it.options
            ]
        }
        JSON.registerObjectMarshaller(TrainLevel) {
            return [
                    id: it.id,
                    level: it.level,
                    dateCreated: it.dateCreated,
                    lastUpdated: it.lastUpdated,
                    questionCount: it.questionCount
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
    }
    def destroy = {
    }
}
