package com.jaovo.common.dao;

import java.util.List;
import java.util.Map;

import com.jaovo.common.model.Pager;

/**
 * 泛型是什么意思?
 * 
 * 是一个标识符,表示说这个地方需要传入某个数据类型.,具体是什么?不知道,让jvm检查一下做个记录
 * 
 * 泛型有什么作用?
 * 
 * 减少类型转换
 * 
 * 任何东西也就是数据和逻辑
 * 
 * 逻辑 : 增删改查
 * 
 * by....有多少属性/表里面有多少个列就有多少个by
 * 
 * like()
 * 
 * in()
 * 
 * ....
 * 
 * 返回结果集
 * 
 * 返回一个单个Object ----> T
 * 
 * list
 * 
 * 数据量过大的话,可以返回一个分页,pager
 * 
 * 模块设计原则 :
 * 
 * 1 满足功能
 * 
 * 2 接口合理
 * 
 * 3 调用舒服
 */
/**
 * 我们的意图是为了所有项目创建一个统一的,通过hibernate访问数据库的通用工具包
 * 
 * 我们定义接口,并通过泛型的方式进行实现,这样就能对多个对象的增删改查都起作用
 * 
 * 为了方便客户端调用,我们尽可能的设计完备接口 1 客户查询的是单个对象
 * 
 * 2 对查询的方法进行增强,查询多个对象返回一个list集合(或者是数组)
 * 
 * 3 查询多个对象,返回多个对象的map 返回一个分页对象, 1 通过hibernate的函数进行增删改查 2
 * 通过hibernate的HQL语句进行增删改查 3 通过原生SQL语句进行增删改查
 * 
 * 实现方式二 : 数据表中记录的数据和对象之间的关系,靠hibernate 实现 实现方式三 ; SQL
 * 我们就只能够获得数据库那边传过来的结果集,但是我们不知道是什么具体的对象
 * 并且,我们还需要满足所有对象的增删改查,我们需要去按照需求,解析结果集,放入到对象,保存到内存
 * 在这里用泛型,并且要求在调用本方法的时候,调用处,出现传入对象类的全名.然后通过反射获得对象
 * 
 * 接口里面用泛型.,但是我们这里相当于一个框架,我实现功能的时候,传入什么对象,一切皆对象,传Object,客户端调用的时候,再向下转型,
 * 
 * 编译时,不运行,这里的object只是一个占位符,用的时候,才传入真实的对象
 * 
 */
public interface IBaseDao<T> {

	/**
	 * 1 简单的单个对象的增删改查.通过调用hibernate的原生函数
	 */
	// 添加对象
	public void add(T t);

	// 根据ID删除对象
	// 为了用户的便捷性,所以这里传的是ID而不是T
	public void delete(int id);

	public void update(T t);

	public T load(int id);

	/**
	 * HQL 语句 : HQL语句传递参数的两种方式 :
	 * 
	 * 第一种参数 : (数据筛选参数) :
	 * 用户定义行,查询具体数据的参数,一般是键值对,键属性名/列,值就是我们要查询满足某个值的对象的值,别名,进行数据筛选
	 * 
	 * 第二种 : (统计处理参数) :
	 * 是我们在排序等对结果集进行处理的时候的参数.这个采纳数就是列/属性名,客户端的需求,排序,分组,过滤(通配符进行统计
	 * ,setParaneter(值)
	 * 
	 * from User where age = :age [order by ? , group by ? having ?];
	 * 
	 * Query.set(age,19); 别名
	 * 
	 * Query.setParameter(name) 通配符
	 * 
	 * 上面HQL中第一部分是参数部分,第二部分是语句部分
	 * 
	 * SQL本质上也是这两种
	 */
	/**
	 * 参数只有通配符的list
	 * 
	 * 1 返回结果是个List,并且参数可以不要,但是最起码也得传入一个HQL语句
	 */
	// 相当于 就是一个 from User; 没有进行数据筛选,也没有进行数据统计
	// 或者是 from User where age = 19;
	public List<T> list(String hql);

	// 相当于 就是一个 from User order by age;
	public List<T> list(String hql, Object args);

	// 相当于 就是一个 from User order by age , group by sex;
	public List<T> list(String hql, Object[] args);

	/**
	 * 2 HQL 语句中,既有通配符.也有别名的list
	 * 
	 * 通过别名以及其对应的数据,进行数据筛选,age=:age 是一个键值对,就是一个map, 键是String 值是对象
	 */
	// 相当于 from User where age = :age;
	public List<T> list(String hql, Map<String, Object> alias);

	// 相当于 from User where age = :age,User order by age,group by sex;
	public List<T> list(String hql, Map<String, Object> alias, Object[] args);

	/**
	 * 3 返回一个Pager分页对象
	 */

	public Pager<T> find(String hql);

	public Pager<T> find(String hql, Object args);

	public Pager<T> find(String hql, Object[] args);

	// 4 .......................HQL语句中 通配符,也有别名的分页
	public Pager<T> find(String hql, Map<String, Object> alias);

	public Pager<T> find(String hql, Object[] args, Map<String, Object> alias);

	/**
	 * 5 返回单个对象的HQL语句查询
	 */
	public Object loadObject(String hql);

	public Object loadObject(String hql, Object args);

	public Object loadObject(String hql, Object[] args);

	public Object loadObject(String hql, Map<String, Object> alias);

	public Object loadObject(String hql, Object[] args,
			Map<String, Object> alias);

	/**
	 * 6 通过HQL语句更新对象 update set 列名=列值; Query query =
	 * getSession().createQuery(hql); 对象.setParameter(args,query);
	 * query.executeUpdate();
	 */
	public void updateByHql(String hql);

	public void updateByHql(String hql, Object args);

	public void updateByHql(String hql, Object[] args);

	// ----------------------------- 需要给酷虎调用设计一个原生的SQL进行数据查询的接口
	// 原生SQL语句,我们只做一个返回List和Pager
	/**
	 * hibernate处理我们的原生SQL语句的过程
	 * 
	 * 我们在进行数据库操作的时候,一个核心要点就是 : 类的属性和表列对应起来
	 * 
	 * 类 ---> 表
	 * 
	 * 类属性 ---> 列名
	 * 
	 * 对象 ---> 具体行中列的值
	 * 
	 * 问题 : 我们上面的时候,用HQL语句,这个时候实际上Hibernate是把对象和表之间绑定的,hql语句中的对象和表默认也是绑定的
	 * 
	 * from User ;
	 * 
	 * 也就是把User ---> t_user绑定
	 * 
	 * 进去查询的时候,没有影响,影响是在查询完以后,这个结果集怎么封装处理的问题
	 * 
	 * 运行时类 : 四大块内存 : 静态代码段 , 栈内存,堆内存,静态数据区 ,运行时类保存在....静态代码段
	 * 
	 * 程序的生命周期 :
	 * 
	 * 写代码,编译(生成xxx.class,class保存在硬盘中),载入内存静态代码段(访问静态属性的时候载入),jvm调用main方法
	 * 
	 * 进入到栈内存阶段,main方法开辟栈帧,进入到方法的链式调用,也就是栈帧的开辟和销毁,如果有new的话就会在堆内存开辟对象的内存空间,保存对象数据
	 * 如果有静态数据的话,载入的时候,就直接把数据保存到静态数据区,并且这些数据在当前上下文生命周期中是公共的,所有对象都可以访问的
	 * 
	 * 但是SQL语句能去吧对象和表绑定吗?我们这里设计的是一个通用的模块,没有办法吧表和对象的对应
	 * 
	 * 由于我们没有用HQL语句,所以也就放弃了Hibernate,也就没有办法进行对象和表之间映射维护
	 * 所以,在往SQL语句中传值的时候,我们需要客户手动传递一个运行时类过来,用于维护实体对象和实体表的映射关系
	 * 
	 * 传参的时候,我们只需要把对象改成运行时类就可以了,运行时类也是集成Object
	 * 
	 * java中一切皆对象,运行时类也是object的子类,那么怎么确定传递的是一个Object对象还是个Class的Object
	 * 所以,我们传一个boolean来指定对象的类型,是个原生的Object还是一个运行时类的Object
	 * 所以.,运行时类要传,之前对象,对象参数也要传,还有就是一个booelan类型的参数,.来指定是运行时类对象还是普通对象
	 * 
	 * 由这三个参数列表公共构成了hibernate内部的对象,类,表的映射体系 这样我们就可以传入任何的SQL语句了
	 * 
	 * 为什么对象要传 : 因为对象里面封装的是我们要操作的数据,但是这个里面的数据,只是数据而已,.没有一个解析的标准,但是不能因此就不要这个数据
	 * 
	 * 所以我们需要再传入运行时类,本质就是解析这些数据的标准
	 * 
	 * 同时我们hibernate,在处理原生的SQL的时候,是需要我们传入运行时类的,这样我们传入的数据能解析,同时返回的结果,
	 * 也会自动封装到我们传入的运行时类的对象中
	 * 
	 * java中 类型擦除,就是对象里面,在传递过程中,有时候只保存了数据,而没有保存类型信息
	 * 
	 * 上面部分的解决了我们返回值封装的问题
	 * 
	 * 返回值怎么处理?问题是我们这里是不确定的类型.,我们怎么表示返回类型,上面分析并没有考虑我们这里是泛型,但是泛型以后,上面就完全不够用了
	 * 
	 * 我们不知道具体类型
	 * 
	 * 但是我们肯定知道返回的是个对象,也就是Object的子类对象
	 * 
	 * 这里的参数,是形参,本质是一个占位符,同时也是jvm进行类型检查的一个标准,是类型检查而不是类型值检查.,
	 * 所以我们只需要知道是某个类型或者是其子类就可以
	 * 
	 * 通过泛型表示一个Object 子类的对象方法 <N extends Object>
	 * 
	 * 上面用的这种方式,意思就是谁都可以,因为一切皆对象,谁都可以传入,就解决了泛型的问题
	 * 
	 * 也解决了返回值的问题,就是返回一个任意类型的对象
	 * 
	 * 我们传入Class clz 的时候.,表示一个运行类,但是这种写法,表示的是一个特性的运行时类,但是我们这里是不特定的
	 * 
	 * 如果不特定,就需要 泛型写法, Class<?> 我们需要传入个运行时类.但是不是特定的,你只需要检查一下,是不是运行时类就可以了,具体是谁不用管
	 * 
	 * boolean hasEntity : 是让客户端传入的参数.,方便我们处理
	 * 
	 */
	/**
	 * 反射 反射的核心 : 通过字符串创建对象,通过字符串调用方法
	 * 
	 * 创建对象的几种方式 :
	 * 
	 * forName(). ***.class , getClass()
	 * 
	 * 方法是什么东西 : 方法是个对象,方法对象对应的运行时类 : java.reflect.Method
	 * 
	 * 方法几部分构成 : 修饰符列表 返回值类型 方法名(形参列表) {方法体}
	 * 
	 * 有几个是必须的 : 返回值 方法名(形参列表) 没有返回值也是返回值
	 * 
	 * 如何唯一性的确定一个方法 : 方法名 形参列表 本质就是方法重载 如何唯一性确定一个方法对象
	 * 
	 * Class c = Class.forName("java.lang.String");
	 * 
	 * Method[] ms = c.getDeclaredMethods();
	 * 
	 * Object o = c.newInstance();
	 * 
	 * Object retValue = ms[0].invoke(o,"aaa","123");
	 * 
	 * 创建了一个没有任何参数没有任何逻辑的一个空的方法对象
	 * 
	 * 所以在调用的时候,需要把数据和逻辑传进去
	 */
	public <N extends Object> List<N> listBySQL(String sql, Class<?> claz,
			boolean hasEntity);

	// 通配符
	// 1 个
	public <N extends Object> List<N> listBySQL(String sql, Object arg,
			Class<?> claz, boolean hasEntity);

	// 多 个
	public <N extends Object> List<N> listBySQL(String sql, Object[] args,
			Class<?> claz, boolean hasEntity);

	// 别名
	public <N extends Object> List<N> listBySQL(String sql,
			Map<String, Object> alias, Class<?> claz, boolean hasEntity);

	// 别名和通配符
	public <N extends Object> List<N> listBySQL(String sql, Object[] args,
			Map<String, Object> alias, Class<?> claz, boolean hasEntity);

	/**
	 * 返回分页
	 */
	public <N extends Object> Pager<N> findBySQL(String sql, Class<?> claz,
			boolean hasEntity);

	public <N extends Object> Pager<N> findBySQL(String sql, Object arg,
			Class<?> claz, boolean hasEntity);

	public <N extends Object> Pager<N> findBySQL(String sql, Object[] args,
			Class<?> claz, boolean hasEntity);

	public <N extends Object> Pager<N> findBySQL(String sql,
			Map<String, Object> alias, Class<?> claz, boolean hasEntity);

	public <N extends Object> Pager<N> findBySQL(String sql, Object[] args,
			Map<String, Object> alias, Class<?> claz, boolean hasEntity);
}
