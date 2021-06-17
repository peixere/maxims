package cn.gotom.commons.utils;

import java.io.UnsupportedEncodingException;
import java.util.Random;

import org.apache.commons.lang3.RandomUtils;

/**
 * @Title: randomName
 * @Description: 随机取名字
 * @param simple 是否单姓
 * @param len    生成姓名长度
 * @return String 名字
 */

public class RandomStringUtils extends org.apache.commons.lang3.RandomStringUtils {

	private static String[] NAMES_EN = { "Aaron", "Abel", "Abraham", "Adam", "Adrian", "Aidan", "Alva", "Alex",
			"Alexander", "Alan", "Albert", "Alfred", "Andrew", "Andy", "Angus", "Anthony", "Apollo", "Arnold", "Arthur",
			"August", "Austin", "Ben", "Benjamin", "Bert", "Benson", "Bill", "Billy", "Blake", "Bob", "Bobby", "Brad",
			"Brandon", "Brant", "Brent", "Brian", "Brown", "Bruce", "Caleb", "Cameron", "Carl", "Carlos", "Cary",
			"Caspar", "Cecil", "Charles", "Cheney", "Chris", "Christian", "Christopher", "Clark", "Cliff", "Cody",
			"Cole", "Colin", "Cosmo", "Daniel", "Denny", "Darwin", "David", "Dennis", "Derek", "Dick", "Donald",
			"Douglas", "Duke", "Dylan", "Eddie", "Edgar", "Edison", "Edmund", "Edward", "Edwin", "Elijah", "Elliott",
			"Elvis", "Eric", "Ethan", "Eugene", "Evan", "Enterprise", "Ford", "Francis", "Frank", "Franklin", "Fred",
			"Gabriel", "Gaby", "Garfield", "Gary", "Gavin", "Geoffrey", "George", "Gino", "Glen", "Glendon", "Hank",
			"Hardy", "Harrison", "Harry", "Hayden", "Henry", "Hilton", "Hugo", "Hunk", "Howard", "Henry", "Ian",
			"Ignativs", "Ivan", "Isaac", "Isaiah", "Jack", "Jackson", "Jacob", "James", "Jason", "Jay", "Jeffery",
			"Jerome", "Jerry", "Jesse", "Jim", "Jimmy", "Joe", "John", "Johnny", "Jonathan", "Jordan", "Jose", "Joshua",
			"Justin", "Keith", "Ken", "Kennedy", "Kenneth", "Kenny", "Kevin", "Kyle", "Lance", "Larry", "Laurent",
			"Lawrence", "Leander", "Lee", "Leo", "Leonard", "Leopold", "Leslie", "Loren", "Lori", "Lorin", "Louis",
			"Luke", "Marcus", "Marcy", "Mark", "Marks", "Mars", "Marshal", "Martin", "Marvin", "Mason", "Matthew",
			"Max", "Michael", "Mickey", "Mike", "Nathan", "Nathaniel", "Neil", "Nelson", "Nicholas", "Nick", "Noah",
			"Norman", "Oliver", "Oscar", "Owen", "Patrick", "Paul", "Peter", "Philip", "Phoebe", "Quentin", "Randall",
			"Randolph", "Randy", "Ray", "Raymond", "Reed", "Rex", "Richard", "Richie", "Riley", "Robert", "Robin",
			"Robinson", "Rock", "Roger", "Ronald", "Rowan", "Roy", "Ryan", "Sam", "Sammy", "Samuel", "Scott", "Sean",
			"Shawn", "Sidney", "Simon", "Solomon", "Spark", "Spencer", "Spike", "Stanley", "Steve", "Steven", "Stewart",
			"Stuart", "Terence", "Terry", "Ted", "Thomas", "Tim", "Timothy", "Todd", "Tommy", "Tom", "Thomas", "Tony",
			"Tyler", "Ultraman", "Ulysses", "Van", "Vern", "Vernon", "Victor", "Vincent", "Warner", "Warren", "Wayne",
			"Wesley", "William", "Willy", "Zack", "Zachary", "Abigail", "Abby", "Ada", "Adelaide", "Adeline",
			"Alexandra", "Ailsa", "Aimee", "Alexis", "Alice", "Alicia", "Alina", "Allison", "Alyssa", "Amanda", "Amy",
			"Amber", "Anastasia", "Andrea", "Angel", "Angela", "Angelia", "Angelina", "Ann", "Anna", "Anne", "Annie",
			"Anita", "Ariel", "April", "Ashley", "Audrey", "Aviva", "Barbara", "Barbie", "Beata", "Beatrice", "Becky",
			"Bella", "Bess", "Bette", "Betty", "Blanche", "Bonnie", "Brenda", "Brianna", "Britney", "Brittany",
			"Camille", "Candice", "Candy", "Carina", "Carmen", "Carol", "Caroline", "Carry", "Carrie", "Cassandra",
			"Cassie", "Catherine", "Cathy", "Chelsea", "Charlene", "Charlotte", "Cherry", "Cheryl", "Chloe", "Chris",
			"Christina", "Christine", "Christy", "Cindy", "Claire", "Claudia", "Clement", "Cloris", "Connie",
			"Constance", "Cora", "Corrine", "Crystal", "Daisy", "Daphne", "Darcy", "Dave", "Debbie", "Deborah", "Debra",
			"Demi", "Diana", "Dolores", "Donna", "Dora", "Doris", "Edith", "Editha", "Elaine", "Eleanor", "Elizabeth",
			"Ella", "Ellen", "Ellie", "Emerald", "Emily", "Emma", "Enid", "Elsa", "Erica", "Estelle", "Esther",
			"Eudora", "Eva", "Eve", "Evelyn", "Fannie", "Fay", "Fiona", "Flora", "Florence", "Frances", "Frederica",
			"Frieda", "Flta", "Gina", "Gillian", "Gladys", "Gloria", "Grace", "Grace", "Greta", "Gwendolyn", "Hannah",
			"Haley", "Hebe", "Helena", "Hellen", "Henna", "Heidi", "Hillary", "Ingrid", "Isabella", "Ishara", "Irene",
			"Iris", "Ivy", "Jacqueline", "Jade", "Jamie", "Jane", "Janet", "Jasmine", "Jean", "Jenna", "Jennifer",
			"Jenny", "Jessica", "Jessie", "Jill", "Joan", "Joanna", "Jocelyn", "Joliet", "Josephine", "Josie", "Joy",
			"Joyce", "Judith", "Judy", "Julia", "Juliana", "Julie", "June", "Karen", "Karida", "Katherine", "Kate",
			"Kathy", "Katie", "Katrina", "Kay", "Kayla", "Kelly", "Kelsey", "Kimberly", "Kitty", "Lareina", "Lassie",
			"Laura", "Lauren", "Lena", "Lydia", "Lillian", "Lily", "Linda", "lindsay", "Lisa", "Liz", "Lora",
			"Lorraine", "Louisa", "Louise", "Lucia", "Lucy", "Lucine", "Lulu", "Lydia", "Lynn", "Mabel", "Madeline",
			"Maggie", "Mamie", "Manda", "Mandy", "Margaret", "Mariah", "Marilyn", "Martha", "Mavis", "Mary", "Matilda",
			"Maureen", "Mavis", "Maxine", "May", "Mayme", "Megan", "Melinda", "Melissa", "Melody", "Mercedes",
			"Meredith", "Mia", "Michelle", "Milly", "Miranda", "Miriam", "Miya", "Molly", "Monica", "Morgan", "Nancy",
			"Natalie", "Natasha", "Nicole", "Nikita", "Nina", "Nora", "Norma", "Nydia", "Octavia", "Olina", "Olivia",
			"Ophelia", "Oprah", "Pamela", "Patricia", "Patty", "Paula", "Pauline", "Pearl", "Peggy", "Philomena",
			"Phoebe", "Phyllis", "Polly", "Priscilla", "Quentina", "Rachel", "Rebecca", "Regina", "Rita", "Rose",
			"Roxanne", "Ruth", "Sabrina", "Sally", "Sandra", "Samantha", "Sami", "Sandra", "Sandy", "Sarah", "Savannah",
			"Scarlett", "Selma", "Selina", "Serena", "Sharon", "Sheila", "Shelley", "Sherry", "Shirley", "Sierra",
			"Silvia", "Sonia", "Sophia", "Stacy", "Stella", "Stephanie", "Sue", "Sunny", "Susan", "Tamara", "Tammy",
			"Tanya", "Tasha", "Teresa", "Tess", "Tiffany", "Tina", "Tonya", "Tracy", "Ursula", "Vanessa", "Venus",
			"Vera", "Vicky", "Victoria", "Violet", "Virginia", "Vita", "Vivian" };

	private static String[] NAMES_ZH = { "亚伦", "亚伯", "亚伯拉罕", "亚当", "艾德里安", "艾登", "阿尔瓦", "亚历克斯", "亚历山大", "艾伦", "艾伯特",
			"阿尔弗雷德", "安德鲁", "安迪", "安格斯", "安东尼", "阿波罗", "阿诺德", "亚瑟", "奥古斯特", "奥斯汀", "本", "本杰明", "伯特", "本森", "比尔", "比利",
			"布莱克", "鲍伯", "鲍比", "布拉德", "布兰登", "布兰特", "布伦特", "布赖恩", "布朗", "布鲁斯", "迦勒", "卡梅伦", "卡尔", "卡洛斯", "凯里", "卡斯帕",
			"塞西", "查尔斯", "采尼", "克里斯", "克里斯蒂安", "克里斯多夫", "克拉克", "柯利弗", "科迪", "科尔", "科林", "科兹莫", "丹尼尔", "丹尼", "达尔文", "大卫",
			"丹尼斯", "德里克", "狄克", "唐纳德", "道格拉斯", "杜克", "迪伦", "埃迪", "埃德加", "爱迪生", "艾德蒙", "爱德华", "艾德文", "以利亚", "艾略特",
			"埃尔维斯", "埃里克", "伊桑", "柳真", "埃文", "企业英语培训", "福特", "弗兰克思", "弗兰克", "富兰克林", "弗瑞德", "加百利", "加比", "加菲尔德", "加里",
			"加文", "杰弗里", "乔治", "基诺", "格林", "格林顿", "汉克", "哈帝", "哈里森", "哈利", "海顿", "亨利", "希尔顿", "雨果", "汉克", "霍华德", "亨利",
			"伊恩", "伊格纳缇伍兹", "伊凡", "艾萨克", "以赛亚", "杰克", "杰克逊", "雅各布", "詹姆士", "詹森", "杰伊", "杰弗瑞", "杰罗姆", "杰瑞", "杰西", "吉姆",
			"吉米", "乔", "约翰", "约翰尼", "乔纳森", "乔丹", "约瑟夫", "约书亚", "贾斯汀", "凯斯", "肯", "肯尼迪", "肯尼斯", "肯尼", "凯文", "凯尔", "兰斯",
			"拉里", "劳伦特", "劳伦斯", "利安德尔", "李", "雷欧", "雷纳德", "利奥波特", "莱斯利", "劳伦", "劳瑞", "劳瑞恩", "路易斯", "路加", "马库斯", "马西",
			"马克", "马科斯", "马尔斯", "马歇尔", "马丁", "马文", "梅森", "马修", "马克斯", "迈克尔", "米奇", "麦克", "内森", "纳撒尼尔", "尼尔", "尼尔森",
			"尼古拉斯", "尼克", "诺亚", "诺曼", "奥利弗", "奥斯卡", "欧文", "帕特里克", "保罗", "彼得", "菲利普", "菲比", "昆廷", "兰德尔", "伦道夫", "兰迪",
			"雷", "雷蒙德", "列得", "雷克斯", "理查德", "里奇", "瑞利", "罗伯特", "罗宾", "鲁宾逊", "洛克", "罗杰", "罗纳", "罗文", "罗伊", "赖安", "萨姆",
			"萨米", "塞缪尔", "斯考特", "肖恩", "肖恩", "西德尼", "西蒙", "所罗门", "斯帕克", "斯宾塞", "斯派克", "斯坦利", "史蒂夫", "史蒂文", "斯图尔特",
			"斯图亚特", "特伦斯", "特里", "泰德", "托马斯", "提姆", "蒂莫西", "托德", "汤米", "汤姆", "托马斯", "托尼", "泰勒", "奥特曼", "尤利塞斯", "范",
			"弗恩", "弗农", "维克多", "文森特", "华纳", "沃伦", "韦恩", "卫斯理", "威廉", "维利", "扎克", "圣扎迦利", "阿比盖尔", "艾比", "艾达", "阿德莱德",
			"艾德琳", "亚历桑德拉", "艾丽莎", "艾米", "亚历克西斯", "爱丽丝", "艾丽西娅", "艾琳娜", "艾莉森", "艾莉莎", "阿曼达", "艾美", "安伯", "阿纳斯塔西娅",
			"安德莉亚", "安琪", "安吉拉", "安吉莉亚", "安吉莉娜", "安", "安娜", "安妮", "安妮", "安尼塔", "艾莉尔", "阿普里尔", "艾许莉", "欧蕊", "阿维娃", "笆笆拉",
			"芭比", "贝亚特", "比阿特丽斯", "贝基", "贝拉", "贝斯", "贝蒂", "贝蒂", "布兰奇", "邦妮", "布伦达", "布莱安娜", "布兰妮", "布列塔尼", "卡米尔", "莰蒂丝",
			"坎蒂", "卡瑞娜", "卡门", "凯罗尔", "卡罗琳", "凯丽", "凯莉", "卡桑德拉", "凯西", "凯瑟琳", "凯茜", "切尔西", "沙琳", "夏洛特", "切莉", "雪莉尔",
			"克洛伊", "克莉丝", "克里斯蒂娜", "克里斯汀", "克里斯蒂", "辛迪", "克莱尔", "克劳迪娅", "克莱门特", "克劳瑞丝", "康妮", "康斯坦斯", "科拉", "科瑞恩",
			"科瑞斯特尔", "戴茜", "达芙妮", "达茜", "戴夫", "黛比", "黛博拉", "黛布拉", "黛米", "黛安娜", "德洛丽丝", "堂娜", "多拉", "桃瑞丝", "伊迪丝", "伊迪萨",
			"伊莱恩", "埃莉诺", "伊丽莎白", "埃拉", "爱伦", "艾莉", "艾米瑞达", "艾米丽", "艾玛", "伊妮德", "埃尔莎", "埃莉卡", "爱斯特尔", "爱丝特", "尤杜拉",
			"伊娃", "伊芙", "伊夫林", "芬妮", "费怡", "菲奥纳", "福罗拉", "弗罗伦丝", "弗郎西丝", "弗雷德里卡", "弗里达", "上海英语外教", "吉娜", "吉莉安", "格拉蒂丝",
			"格罗瑞娅", "格瑞丝", "格瑞丝", "格瑞塔", "格温多琳", "汉娜", "海莉", "赫柏", "海伦娜", "海伦", "汉纳", "海蒂", "希拉里", "英格丽德", "伊莎贝拉",
			"爱沙拉", "艾琳", "艾丽丝", "艾维", "杰奎琳", "小玉", "詹米", "简", "珍妮特", "贾斯敏", "姬恩", "珍娜", "詹妮弗", "詹妮", "杰西卡", "杰西", "姬尔",
			"琼", "乔安娜", "乔斯林", "乔莉埃特", "约瑟芬", "乔茜", "乔伊", "乔伊斯", "朱迪丝", "朱蒂", "朱莉娅", "朱莉安娜", "朱莉", "朱恩", "凯琳", "卡瑞达",
			"凯瑟琳", "凯特", "凯西", "卡蒂", "卡特里娜", "凯", "凯拉", "凯莉", "凯尔西", "特里娜", "基蒂", "莱瑞拉", "蕾西", "劳拉", "罗兰", "莉娜", "莉迪娅",
			"莉莲", "莉莉", "琳达", "琳赛", "丽莎", "莉兹", "洛拉", "罗琳", "路易莎", "路易丝", "露西娅", "露茜", "露西妮", "露露", "莉迪娅", "林恩", "梅布尔",
			"马德琳", "玛姬", "玛米", "曼达", "曼迪", "玛格丽特", "玛丽亚", "玛丽莲", "玛莎", "梅维丝", "玛丽", "玛蒂尔达", "莫琳", "梅维丝", "玛克辛", "梅",
			"梅米", "梅甘", "梅琳达", "梅利莎", "美洛蒂", "默西迪丝", "梅瑞狄斯", "米娅", "米歇尔", "米莉", "米兰达", "米里亚姆", "米娅", "茉莉", "莫尼卡", "摩根",
			"南茜", "娜塔莉", "娜塔莎", "妮可", "尼基塔", "尼娜", "诺拉", "诺玛", "尼迪亚", "奥克塔维亚", "奥琳娜", "奥利维亚", "奥菲莉娅", "奥帕", "帕梅拉",
			"帕特丽夏", "芭迪", "保拉", "波琳", "珀尔", "帕姬", "菲洛米娜", "菲比", "菲丽丝", "波莉", "普里西拉", "昆蒂娜", "雷切尔", "丽贝卡", "瑞加娜", "丽塔",
			"罗丝", "洛克萨妮", "露丝", "萨布丽娜", "萨莉", "桑德拉", "萨曼莎", "萨米", "桑德拉", "桑迪", "莎拉", "萨瓦纳", "斯佳丽", "塞尔玛", "塞琳娜", "塞丽娜",
			"莎伦", "希拉", "雪莉", "雪丽", "雪莉", "斯莱瑞", "西尔维亚", "索尼亚", "索菲娅", "丝塔茜", "丝特拉", "斯蒂芬妮", "苏", "萨妮", "苏珊", "塔玛拉",
			"苔米", "谭雅坦尼娅", "塔莎", "特莉萨", "苔丝", "蒂凡妮", "蒂娜", "棠雅", "特蕾西", "厄休拉", "温妮莎", "维纳斯", "维拉", "维姬", "维多利亚", "维尔莉特",
			"维吉妮亚", "维达", "薇薇安" };

	private static String[] SUR_NAME = { "赵", "钱", "孙", "李", "周", "吴", "郑", "王", "冯", "陈", "楮", "卫", "蒋", "沈", "韩", "杨",
			"朱", "秦", "尤", "许", "何", "吕", "施", "张", "孔", "曹", "严", "华", "金", "魏", "陶", "姜", "戚", "谢", "邹", "喻", "柏",
			"水", "窦", "章", "云", "苏", "潘", "葛", "奚", "范", "彭", "郎", "鲁", "韦", "昌", "马", "苗", "凤", "花", "方", "俞", "任",
			"袁", "柳", "酆", "鲍", "史", "唐", "费", "廉", "岑", "薛", "雷", "贺", "倪", "汤", "滕", "殷", "罗", "毕", "郝", "邬", "安",
			"常", "乐", "于", "时", "傅", "皮", "卞", "齐", "康", "伍", "余", "元", "卜", "顾", "孟", "平", "黄", "和", "穆", "萧", "尹",
			"姚", "邵", "湛", "汪", "祁", "毛", "禹", "狄", "米", "贝", "明", "臧", "计", "伏", "成", "戴", "谈", "宋", "茅", "庞", "熊",
			"纪", "舒", "屈", "项", "祝", "董", "梁", "杜", "阮", "蓝", "闽", "席", "季", "麻", "强", "贾", "路", "娄", "危", "江", "童",
			"颜", "郭", "梅", "盛", "林", "刁", "锺", "徐", "丘", "骆", "高", "夏", "蔡", "田", "樊", "胡", "凌", "霍", "虞", "万", "支",
			"柯", "昝", "管", "卢", "莫", "经", "房", "裘", "缪", "干", "解", "应", "宗", "丁", "宣", "贲", "邓", "郁", "单", "杭", "洪",
			"包", "诸", "左", "石", "崔", "吉", "钮", "龚", "程", "嵇", "邢", "滑", "裴", "陆", "荣", "翁", "荀", "羊", "於", "惠", "甄",
			"麹", "家", "封", "芮", "羿", "储", "靳", "汲", "邴", "糜", "松", "井", "段", "富", "巫", "乌", "焦", "巴", "弓", "牧", "隗",
			"山", "谷", "车", "侯", "宓", "蓬", "全", "郗", "班", "仰", "秋", "仲", "伊", "宫", "宁", "仇", "栾", "暴", "甘", "斜", "厉",
			"戎", "祖", "武", "符", "刘", "景", "詹", "束", "龙", "叶", "幸", "司", "韶", "郜", "黎", "蓟", "薄", "印", "宿", "白", "怀",
			"蒲", "邰", "从", "鄂", "索", "咸", "籍", "赖", "卓", "蔺", "屠", "蒙", "池", "乔", "阴", "郁", "胥", "能", "苍", "双", "闻",
			"莘", "党", "翟", "谭", "贡", "劳", "逄", "姬", "申", "扶", "堵", "冉", "宰", "郦", "雍", "郤", "璩", "桑", "桂", "濮", "牛",
			"寿", "通", "边", "扈", "燕", "冀", "郏", "浦", "尚", "农", "温", "别", "庄", "晏", "柴", "瞿", "阎", "充", "慕", "连", "茹",
			"习", "宦", "艾", "鱼", "容", "向", "古", "易", "慎", "戈", "廖", "庾", "终", "暨", "居", "衡", "步", "都", "耿", "满", "弘",
			"匡", "国", "文", "寇", "广", "禄", "阙", "东", "欧", "殳", "沃", "利", "蔚", "越", "夔", "隆", "师", "巩", "厍", "聂", "晁",
			"勾", "敖", "融", "冷", "訾", "辛", "阚", "那", "简", "饶", "空", "曾", "毋", "沙", "乜", "养", "鞠", "须", "丰", "巢", "关",
			"蒯", "相", "查", "后", "荆", "红", "游", "竺", "权", "逑", "盖", "益", "桓", "公", "晋", "楚", "阎", "法", "汝", "鄢", "涂",
			"钦", "岳", "帅", "缑", "亢", "况", "后", "有", "琴", "商", "牟", "佘", "佴", "伯", "赏", "墨", "哈", "谯", "笪", "年", "爱",
			"阳", "佟" };

	private static String[] DOUBLE_SUR_NAME = { "万俟", "司马", "上官", "欧阳", "夏侯", "诸葛", "闻人", "东方", "赫连", "皇甫", "尉迟", "公羊",
			"澹台", "公冶", "宗政", "濮阳", "淳于", "单于", "太叔", "申屠", "公孙", "仲孙", "轩辕", "令狐", "锺离", "宇文", "长孙", "慕容", "鲜于", "闾丘",
			"司徒", "司空", "丌官", "司寇", "仉", "督", "子车", "颛孙", "端木", "巫马", "公西", "漆雕", "乐正", "壤驷", "公良", "拓拔", "夹谷", "宰父",
			"谷梁", "段干", "百里", "东郭", "南门", "呼延", "归", "海", "羊舌", "微生", "梁丘", "左丘", "东门", "西门", "南宫" };

	private static String[] NAME_WORD = { "一", "乙", "二", "十", "丁", "厂", "七", "卜", "人", "入", "八", "九", "几", "儿", "了",
			"力", "乃", "刀", "又", "三", "于", "干", "亏", "士", "工", "土", "才", "寸", "下", "大", "丈", "与", "万", "上", "小", "口",
			"巾", "山", "千", "乞", "川", "亿", "个", "勺", "久", "凡", "及", "夕", "丸", "么", "广", "亡", "门", "义", "之", "尸", "弓",
			"己", "已", "子", "卫", "也", "女", "飞", "刃", "习", "叉", "马", "乡", "丰", "王", "井", "开", "夫", "天", "无", "元", "专",
			"云", "扎", "艺", "木", "五", "支", "厅", "不", "太", "犬", "区", "历", "尤", "友", "匹", "车", "巨", "牙", "屯", "比", "互",
			"切", "瓦", "止", "少", "日", "中", "冈", "贝", "内", "水", "见", "午", "牛", "手", "毛", "气", "升", "长", "仁", "什", "片",
			"仆", "化", "仇", "币", "仍", "仅", "斤", "爪", "反", "介", "父", "从", "今", "凶", "分", "乏", "公", "仓", "月", "氏", "勿",
			"欠", "风", "丹", "匀", "乌", "凤", "勾", "文", "六", "方", "火", "为", "斗", "忆", "订", "计", "户", "认", "心", "尺", "引",
			"丑", "巴", "孔", "队", "办", "以", "允", "予", "劝", "双", "书", "幻", "玉", "刊", "示", "末", "未", "击", "打", "巧", "正",
			"扑", "扒", "功", "扔", "去", "甘", "世", "古", "节", "本", "术", "可", "丙", "左", "厉", "右", "石", "布", "龙", "平", "灭",
			"轧", "东", "卡", "北", "占", "业", "旧", "帅", "归", "且", "旦", "目", "叶", "甲", "申", "叮", "电", "号", "田", "由", "史",
			"只", "央", "兄", "叼", "叫", "另", "叨", "叹", "四", "生", "失", "禾", "丘", "付", "仗", "代", "仙", "们", "仪", "白", "仔",
			"他", "斥", "瓜", "乎", "丛", "令", "用", "甩", "印", "乐", "句", "匆", "册", "犯", "外", "处", "冬", "鸟", "务", "包", "饥",
			"主", "市", "立", "闪", "兰", "半", "汁", "汇", "头", "汉", "宁", "穴", "它", "讨", "写", "让", "礼", "训", "必", "议", "讯",
			"记", "永", "司", "尼", "民", "出", "辽", "奶", "奴", "加", "召", "皮", "边", "发", "孕", "圣", "对", "台", "矛", "纠", "母",
			"幼", "丝", "式", "刑", "动", "扛", "寺", "吉", "扣", "考", "托", "老", "执", "巩", "圾", "扩", "扫", "地", "扬", "场", "耳",
			"共", "芒", "亚", "芝", "朽", "朴", "机", "权", "过", "臣", "再", "协", "西", "压", "厌", "在", "有", "百", "存", "而", "页",
			"匠", "夸", "夺", "灰", "达", "列", "死", "成", "夹", "轨", "邪", "划", "迈", "毕", "至", "此", "贞", "师", "尘", "尖", "劣",
			"光", "当", "早", "吐", "吓", "虫", "曲", "团", "同", "吊", "吃", "因", "吸", "吗", "屿", "帆", "岁", "回", "岂", "刚", "则",
			"肉", "网", "年", "朱", "先", "丢", "舌", "竹", "迁", "乔", "伟", "传", "乒", "乓", "休", "伍", "伏", "优", "伐", "延", "件",
			"任", "伤", "价", "份", "华", "仰", "仿", "伙", "伪", "自", "血", "向", "似", "后", "行", "舟", "全", "会", "杀", "合", "兆",
			"企", "众", "爷", "伞", "创", "肌", "朵", "杂", "危", "旬", "旨", "负", "各", "名", "多", "争", "色", "壮", "冲", "冰", "庄",
			"庆", "亦", "刘", "齐", "交", "次", "衣", "产", "决", "充", "妄", "闭", "问", "闯", "羊", "并", "关", "米", "灯", "州", "汗",
			"污", "江", "池", "汤", "忙", "兴", "宇", "守", "宅", "字", "安", "讲", "军", "许", "论", "农", "讽", "设", "访", "寻", "那",
			"迅", "尽", "导", "异", "孙", "阵", "阳", "收", "阶", "阴", "防", "奸", "如", "妇", "好", "她", "妈", "戏", "羽", "观", "欢",
			"买", "红", "纤", "级", "约", "纪", "驰", "巡", "寿", "弄", "麦", "形", "进", "戒", "吞", "远", "违", "运", "扶", "抚", "坛",
			"技", "坏", "扰", "拒", "找", "批", "扯", "址", "走", "抄", "坝", "贡", "攻", "赤", "折", "抓", "扮", "抢", "孝", "均", "抛",
			"投", "坟", "抗", "坑", "坊", "抖", "护", "壳", "志", "扭", "块", "声", "把", "报", "却", "劫", "芽", "花", "芹", "芬", "苍",
			"芳", "严", "芦", "劳", "克", "苏", "杆", "杠", "杜", "材", "村", "杏", "极", "李", "杨", "求", "更", "束", "豆", "两", "丽",
			"医", "辰", "励", "否", "还", "歼", "来", "连", "步", "坚", "旱", "盯", "呈", "时", "吴", "助", "县", "里", "呆", "园", "旷",
			"围", "呀", "吨", "足", "邮", "男", "困", "吵", "串", "员", "听", "吩", "吹", "呜", "吧", "吼", "别", "岗", "帐", "财", "针",
			"钉", "告", "我", "乱", "利", "秃", "秀", "私", "每", "兵", "估", "体", "何", "但", "伸", "作", "伯", "伶", "佣", "低", "你",
			"住", "位", "伴", "身", "皂", "佛", "近", "彻", "役", "返", "余", "希", "坐", "谷", "妥", "含", "邻", "岔", "肝", "肚", "肠",
			"龟", "免", "狂", "犹", "角", "删", "条", "卵", "岛", "迎", "饭", "饮", "系", "言", "冻", "状", "亩", "况", "床", "库", "疗",
			"应", "冷", "这", "序", "辛", "弃", "冶", "忘", "闲", "间", "闷", "判", "灶", "灿", "弟", "汪", "沙", "汽", "沃", "泛", "沟",
			"没", "沈", "沉", "怀", "忧", "快", "完", "宋", "宏", "牢", "究", "穷", "灾", "良", "证", "启", "评", "补", "初", "社", "识",
			"诉", "诊", "词", "译", "君", "灵", "即", "层", "尿", "尾", "迟", "局", "改", "张", "忌", "际", "陆", "阿", "陈", "阻", "附",
			"妙", "妖", "妨", "努", "忍", "劲", "鸡", "驱", "纯", "纱", "纳", "纲", "驳", "纵", "纷", "纸", "纹", "纺", "驴", "纽", "奉",
			"玩", "环", "武", "青", "责", "现", "表", "规", "抹", "拢", "拔", "拣", "担", "坦", "押", "抽", "拐", "拖", "拍", "者", "顶",
			"拆", "拥", "抵", "拘", "势", "抱", "垃", "拉", "拦", "拌", "幸", "招", "坡", "披", "拨", "择", "抬", "其", "取", "苦", "若",
			"茂", "苹", "苗", "英", "范", "直", "茄", "茎", "茅", "林", "枝", "杯", "柜", "析", "板", "松", "枪", "构", "杰", "述", "枕",
			"丧", "或", "画", "卧", "事", "刺", "枣", "雨", "卖", "矿", "码", "厕", "奔", "奇", "奋", "态", "欧", "垄", "妻", "轰", "顷",
			"转", "斩", "轮", "软", "到", "非", "叔", "肯", "齿", "些", "虎", "虏", "肾", "贤", "尚", "旺", "具", "果", "味", "昆", "国",
			"昌", "畅", "明", "易", "昂", "典", "固", "忠", "咐", "呼", "鸣", "咏", "呢", "岸", "岩", "帖", "罗", "帜", "岭", "凯", "败",
			"贩", "购", "图", "钓", "制", "知", "垂", "牧", "物", "乖", "刮", "秆", "和", "季", "委", "佳", "侍", "供", "使", "例", "版",
			"侄", "侦", "侧", "凭", "侨", "佩", "货", "依", "的", "迫", "质", "欣", "征", "往", "爬", "彼", "径", "所", "舍", "金", "命",
			"斧", "爸", "采", "受", "乳", "贪", "念", "贫", "肤", "肺", "肢", "肿", "胀", "朋", "股", "肥", "服", "胁", "周", "昏", "鱼",
			"兔", "狐", "忽", "狗", "备", "饰", "饱", "饲", "变", "京", "享", "店", "夜", "庙", "府", "底", "剂", "郊", "废", "净", "盲",
			"放", "刻", "育", "闸", "闹", "郑", "券", "卷", "单", "炒", "炊", "炕", "炎", "炉", "沫", "浅", "法", "泄", "河", "沾", "泪",
			"油", "泊", "沿", "泡", "注", "泻", "泳", "泥", "沸", "波", "泼", "泽", "治", "怖", "性", "怕", "怜", "怪", "学", "宝", "宗",
			"定", "宜", "审", "宙", "官", "空", "帘", "实", "试", "郎", "诗", "肩", "房", "诚", "衬", "衫", "视", "话", "诞", "询", "该",
			"详", "建", "肃", "录", "隶", "居", "届", "刷", "屈", "弦", "承", "孟", "孤", "陕", "降", "限", "妹", "姑", "姐", "姓", "始",
			"驾", "参", "艰", "线", "练", "组", "细", "驶", "织", "终", "驻", "驼", "绍", "经", "贯", "奏", "春", "帮", "珍", "玻", "毒",
			"型", "挂", "封", "持", "项", "垮", "挎", "城", "挠", "政", "赴", "赵", "挡", "挺", "括", "拴", "拾", "挑", "指", "垫", "挣",
			"挤", "拼", "挖", "按", "挥", "挪", "某", "甚", "革", "荐", "巷", "带", "草", "茧", "茶", "荒", "茫", "荡", "荣", "故", "胡",
			"南", "药", "标", "枯", "柄", "栋", "相", "查", "柏", "柳", "柱", "柿", "栏", "树", "要", "咸", "威", "歪", "研", "砖", "厘",
			"厚", "砌", "砍", "面", "耐", "耍", "牵", "残", "殃", "轻", "鸦", "皆", "背", "战", "点", "临", "览", "竖", "省", "削", "尝",
			"是", "盼", "眨", "哄", "显", "哑", "冒", "映", "星", "昨", "畏", "趴", "胃", "贵", "界", "虹", "虾", "蚁", "思", "蚂", "虽",
			"品", "咽", "骂", "哗", "咱", "响", "哈", "咬", "咳", "哪", "炭", "峡", "罚", "贱", "贴", "骨", "钞", "钟", "钢", "钥", "钩",
			"卸", "缸", "拜", "看", "矩", "怎", "牲", "选", "适", "秒", "香", "种", "秋", "科", "重", "复", "竿", "段", "便", "俩", "贷",
			"顺", "修", "保", "促", "侮", "俭", "俗", "俘", "信", "皇", "泉", "鬼", "侵", "追", "俊", "盾", "待", "律", "很", "须", "叙",
			"剑", "逃", "食", "盆", "胆", "胜", "胞", "胖", "脉", "勉", "狭", "狮", "独", "狡", "狱", "狠", "贸", "怨", "急", "饶", "蚀",
			"饺", "饼", "弯", "将", "奖", "哀", "亭", "亮", "度", "迹", "庭", "疮", "疯", "疫", "疤", "姿", "亲", "音", "帝", "施", "闻",
			"阀", "阁", "差", "养", "美", "姜", "叛", "送", "类", "迷", "前", "首", "逆", "总", "炼", "炸", "炮", "烂", "剃", "洁", "洪",
			"洒", "浇", "浊", "洞", "测", "洗", "活", "派", "洽", "染", "济", "洋", "洲", "浑", "浓", "津", "恒", "恢", "恰", "恼", "恨",
			"举", "觉", "宣", "室", "宫", "宪", "突", "穿", "窃", "客", "冠", "语", "扁", "袄", "祖", "神", "祝", "误", "诱", "说", "诵",
			"垦", "退", "既", "屋", "昼", "费", "陡", "眉", "孩", "除", "险", "院", "娃", "姥", "姨", "姻", "娇", "怒", "架", "贺", "盈",
			"勇", "怠", "柔", "垒", "绑", "绒", "结", "绕", "骄", "绘", "给", "络", "骆", "绝", "绞", "统", "耕", "耗", "艳", "泰", "珠",
			"班", "素", "蚕", "顽", "盏", "匪", "捞", "栽", "捕", "振", "载", "赶", "起", "盐", "捎", "捏", "埋", "捉", "捆", "捐", "损",
			"都", "哲", "逝", "捡", "换", "挽", "热", "恐", "壶", "挨", "耻", "耽", "恭", "莲", "莫", "荷", "获", "晋", "恶", "真", "框",
			"桂", "档", "桐", "株", "桥", "桃", "格", "校", "核", "样", "根", "索", "哥", "速", "逗", "栗", "配", "翅", "辱", "唇", "夏",
			"础", "破", "原", "套", "逐", "烈", "殊", "顾", "轿", "较", "顿", "毙", "致", "柴", "桌", "虑", "监", "紧", "党", "晒", "眠",
			"晓", "鸭", "晃", "晌", "晕", "蚊", "哨", "哭", "恩", "唤", "啊", "唉", "罢", "峰", "圆", "贼", "贿", "钱", "钳", "钻", "铁",
			"铃", "铅", "缺", "氧", "特", "牺", "造", "乘", "敌", "秤", "租", "积", "秧", "秩", "称", "秘", "透", "笔", "笑", "笋", "债",
			"借", "值", "倚", "倾", "倒", "倘", "俱", "倡", "候", "俯", "倍", "倦", "健", "臭", "射", "躬", "息", "徒", "徐", "舰", "舱",
			"般", "航", "途", "拿", "爹", "爱", "颂", "翁", "脆", "脂", "胸", "胳", "脏", "胶", "脑", "狸", "狼", "逢", "留", "皱", "饿",
			"恋", "桨", "浆", "衰", "高", "席", "准", "座", "脊", "症", "病", "疾", "疼", "疲", "效", "离", "唐", "资", "凉", "站", "剖",
			"竞", "部", "旁", "旅", "畜", "阅", "羞", "瓶", "拳", "粉", "料", "益", "兼", "烤", "烘", "烦", "烧", "烛", "烟", "递", "涛",
			"浙", "涝", "酒", "涉", "消", "浩", "海", "涂", "浴", "浮", "流", "润", "浪", "浸", "涨", "烫", "涌", "悟", "悄", "悔", "悦",
			"害", "宽", "家", "宵", "宴", "宾", "窄", "容", "宰", "案", "请", "朗", "诸", "读", "扇", "袜", "袖", "袍", "被", "祥", "课",
			"谁", "调", "冤", "谅", "谈", "谊", "剥", "恳", "展", "剧", "屑", "弱", "陵", "陶", "陷", "陪", "娱", "娘", "通", "能", "难",
			"预", "桑", "绢", "绣", "验", "继", "球", "理", "捧", "堵", "描", "域", "掩", "捷", "排", "掉", "堆", "推", "掀", "授", "教",
			"掏", "掠", "培", "接", "控", "探", "据", "掘", "职", "基", "著", "勒", "黄", "萌", "萝", "菌", "菜", "萄", "菊", "萍", "菠",
			"营", "械", "梦", "梢", "梅", "检", "梳", "梯", "桶", "救", "副", "票", "戚", "爽", "聋", "袭", "盛", "雪", "辅", "辆", "虚",
			"雀", "堂", "常", "匙", "晨", "睁", "眯", "眼", "悬", "野", "啦", "晚", "啄", "距", "跃", "略", "蛇", "累", "唱", "患", "唯",
			"崖", "崭", "崇", "圈", "铜", "铲", "银", "甜", "梨", "犁", "移", "笨", "笼", "笛", "符", "第", "敏", "做", "袋", "悠", "偿",
			"偶", "偷", "您", "售", "停", "偏", "假", "得", "衔", "盘", "船", "斜", "盒", "鸽", "悉", "欲", "彩", "领", "脚", "脖", "脸",
			"脱", "象", "够", "猜", "猪", "猎", "猫", "猛", "馅", "馆", "凑", "减", "毫", "麻", "痒", "痕", "廊", "康", "庸", "鹿", "盗",
			"章", "竟", "商", "族", "旋", "望", "率", "着", "盖", "粘", "粗", "粒", "断", "剪", "兽", "清", "添", "淋", "淹", "渠", "渐",
			"混", "渔", "淘", "液", "淡", "深", "婆", "梁", "渗", "情", "惜", "惭", "悼", "惧", "惕", "惊", "惨", "惯", "寇", "寄", "宿",
			"窑", "密", "谋", "谎", "祸", "谜", "逮", "敢", "屠", "弹", "随", "蛋", "隆", "隐", "婚", "婶", "颈", "绩", "绪", "续", "骑",
			"绳", "维", "绵", "绸", "绿", "琴", "斑", "替", "款", "堪", "搭", "塔", "越", "趁", "趋", "超", "提", "堤", "博", "揭", "喜",
			"插", "揪", "搜", "煮", "援", "裁", "搁", "搂", "搅", "握", "揉", "斯", "期", "欺", "联", "散", "惹", "葬", "葛", "董", "葡",
			"敬", "葱", "落", "朝", "辜", "葵", "棒", "棋", "植", "森", "椅", "椒", "棵", "棍", "棉", "棚", "棕", "惠", "惑", "逼", "厨",
			"厦", "硬", "确", "雁", "殖", "裂", "雄", "暂", "雅", "辈", "悲", "紫", "辉", "敞", "赏", "掌", "晴", "暑", "最", "量", "喷",
			"晶", "喇", "遇", "喊", "景", "践", "跌", "跑", "遗", "蛙", "蛛", "蜓", "喝", "喂", "喘", "喉", "幅", "帽", "赌", "赔", "黑",
			"铸", "铺", "链", "销", "锁", "锄", "锅", "锈", "锋", "锐", "短", "智", "毯", "鹅", "剩", "稍", "程", "稀", "税", "筐", "等",
			"筑", "策", "筛", "筒", "答", "筋", "筝", "傲", "傅", "牌", "堡", "集", "焦", "傍", "储", "奥", "街", "惩", "御", "循", "艇",
			"舒", "番", "释", "禽", "腊", "脾", "腔", "鲁", "猾", "猴", "然", "馋", "装", "蛮", "就", "痛", "童", "阔", "善", "羡", "普",
			"粪", "尊", "道", "曾", "焰", "港", "湖", "渣", "湿", "温", "渴", "滑", "湾", "渡", "游", "滋", "溉", "愤", "慌", "惰", "愧",
			"愉", "慨", "割", "寒", "富", "窜", "窝", "窗", "遍", "裕", "裤", "裙", "谢", "谣", "谦", "属", "屡", "强", "粥", "疏", "隔",
			"隙", "絮", "嫂", "登", "缎", "缓", "编", "骗", "缘", "瑞", "魂", "肆", "摄", "摸", "填", "搏", "塌", "鼓", "摆", "携", "搬",
			"摇", "搞", "塘", "摊", "蒜", "勤", "鹊", "蓝", "墓", "幕", "蓬", "蓄", "蒙", "蒸", "献", "禁", "楚", "想", "槐", "榆", "楼",
			"概", "赖", "酬", "感", "碍", "碑", "碎", "碰", "碗", "碌", "雷", "零", "雾", "雹", "输", "督", "龄", "鉴", "睛", "睡", "睬",
			"鄙", "愚", "暖", "盟", "歇", "暗", "照", "跨", "跳", "跪", "路", "跟", "遣", "蛾", "蜂", "嗓", "置", "罪", "罩", "错", "锡",
			"锣", "锤", "锦", "键", "锯", "矮", "辞", "稠", "愁", "筹", "签", "简", "毁", "舅", "鼠", "催", "傻", "像", "躲", "微", "愈",
			"遥", "腰", "腥", "腹", "腾", "腿", "触", "解", "酱", "痰", "廉", "新", "韵", "意", "粮", "数", "煎", "塑", "慈", "煤", "煌",
			"满", "漠", "源", "滤", "滥", "滔", "溪", "溜", "滚", "滨", "粱", "滩", "慎", "誉", "塞", "谨", "福", "群", "殿", "辟", "障",
			"嫌", "嫁", "叠", "缝", "缠", "静", "碧", "璃", "墙", "撇", "嘉", "摧", "截", "誓", "境", "摘", "摔", "聚", "蔽", "慕", "暮",
			"蔑", "模", "榴", "榜", "榨", "歌", "遭", "酷", "酿", "酸", "磁", "愿", "需", "弊", "裳", "颗", "嗽", "蜻", "蜡", "蝇", "蜘",
			"赚", "锹", "锻", "舞", "稳", "算", "箩", "管", "僚", "鼻", "魄", "貌", "膜", "膊", "膀", "鲜", "疑", "馒", "裹", "敲", "豪",
			"膏", "遮", "腐", "瘦", "辣", "竭", "端", "旗", "精", "歉", "熄", "熔", "漆", "漂", "漫", "滴", "演", "漏", "慢", "寨", "赛",
			"察", "蜜", "谱", "嫩", "翠", "熊", "凳", "骡", "缩", "慧", "撕", "撒", "趣", "趟", "撑", "播", "撞", "撤", "增", "聪", "鞋",
			"蕉", "蔬", "横", "槽", "樱", "橡", "飘", "醋", "醉", "震", "霉", "瞒", "题", "暴", "瞎", "影", "踢", "踏", "踩", "踪", "蝶",
			"蝴", "嘱", "墨", "镇", "靠", "稻", "黎", "稿", "稼", "箱", "箭", "篇", "僵", "躺", "僻", "德", "艘", "膝", "膛", "熟", "摩",
			"颜", "毅", "糊", "遵", "潜", "潮", "懂", "额", "慰", "劈", "操", "燕", "薯", "薪", "薄", "颠", "橘", "整", "融", "醒", "餐",
			"嘴", "蹄", "器", "赠", "默", "镜", "赞", "篮", "邀", "衡", "膨", "雕", "磨", "凝", "辨", "辩", "糖", "糕", "燃", "澡", "激",
			"懒", "壁", "避", "缴", "戴", "擦", "鞠", "藏", "霜", "霞", "瞧", "蹈", "螺", "穗", "繁", "辫", "赢", "糟", "糠", "燥", "臂",
			"翼", "骤", "鞭", "覆", "蹦", "镰", "翻", "鹰", "警", "攀", "蹲", "颤", "瓣", "爆", "疆", "壤", "耀", "躁", "嚼", "嚷", "籍",
			"魔", "灌", "蠢", "霸", "露", "囊", "罐" };

	private static final String SOURCES = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890";

	public static String random() {
		int num = RandomUtils.nextInt(0, 5);
		switch (num) {
		case 0:
			return randomStoreEn();
		case 1:
			return randomStoreZh();
		case 2:
			return randomEn(8);
		case 3:
			return randomZh(8);
		case 4:
			return randomStoreEn();
		default:
			return randomZh(RandomUtils.nextInt(0, 1) == 1, 8);
		}
	}

	/**
	 * 随机生成英文名
	 * 
	 * @param length
	 * @return
	 */
	public static String randomStoreEn() {
		return NAMES_EN[RandomUtils.nextInt(0, NAMES_EN.length)];
	}

	/**
	 * 随机生成英文名
	 * 
	 * @param length
	 * @return
	 */
	public static String randomStoreZh() {
		return NAMES_ZH[RandomUtils.nextInt(0, NAMES_ZH.length)];
	}

	/**
	 * 随机生成英文名
	 * 
	 * @param length
	 * @return
	 */
	public static String randomEn(int maxLen) {
		int length = RandomUtils.nextInt(8, maxLen + 1);
		int size = length / 2;
		int len = length % 2 == 0 ? size : size + 1;
		char[] text = new char[len];
		for (int i = 0; i < text.length; i++) {
			text[i] = SOURCES.charAt(RandomUtils.nextInt(0, SOURCES.length()));
		}
		int number = 1;
		for (int i = 1; i < size; i++) {
			number = number * 10;
		}
		return new String(text) + RandomUtils.nextInt(number, number * 10);
	}

	/**
	 * 随机生成中文名
	 * 
	 * @return
	 */
	public static String randomZh(int maxLen) {
		int len = RandomUtils.nextInt(2, maxLen + 1);
		String randomName = "";
		for (int i = 0; i < len; i++) {
			String str = "";
			int hightPos, lowPos; // 定义高低位
			Random random = new Random();
			hightPos = (176 + Math.abs(random.nextInt(39))); // 获取高位值
			lowPos = (161 + Math.abs(random.nextInt(93))); // 获取低位值
			byte[] b = new byte[2];
			b[0] = Integer.valueOf(hightPos).byteValue();
			b[1] = Integer.valueOf(lowPos).byteValue();
			try {
				str = new String(b, "GBK"); // 转成中文
			} catch (UnsupportedEncodingException ex) {
				;
			}
			randomName += str;
		}
		return randomName;
	}

	/**
	 * 在姓名库中随机生成中文名
	 * 
	 * @param simple
	 * @return
	 */
	public static String randomZh(boolean simple, int maxLen) {
		int len = RandomUtils.nextInt(2, maxLen + 1);
		int surNameLen = SUR_NAME.length;
		int doubleSurNameLen = DOUBLE_SUR_NAME.length;
		int wordLen = NAME_WORD.length;

		StringBuffer sb = new StringBuffer();
		Random random = new Random();
		if (simple) {
			sb.append(SUR_NAME[random.nextInt(surNameLen)]);
			int surLen = sb.toString().length();
			for (int i = 0; i < len - surLen; i++) {
				if (sb.toString().length() <= len) {
					sb.append(NAME_WORD[random.nextInt(wordLen)]);
				}
			}
		} else {
			sb.append(DOUBLE_SUR_NAME[random.nextInt(doubleSurNameLen)]);
			int doubleSurLen = sb.toString().length();
			for (int i = 0; i < len - doubleSurLen; i++) {
				if (sb.toString().length() <= len) {
					sb.append(NAME_WORD[random.nextInt(wordLen)]);
				}
			}
		}
		return sb.toString();
	}
}