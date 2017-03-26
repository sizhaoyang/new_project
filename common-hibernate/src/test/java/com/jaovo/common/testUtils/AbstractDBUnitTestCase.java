package com.jaovo.common.testUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;

import org.dbunit.DatabaseUnitException;
import org.dbunit.database.AmbiguousTableNameException;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.database.QueryDataSet;
import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatDtdDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSet;
import org.dbunit.dataset.xml.FlatXmlProducer;
import org.dbunit.operation.DatabaseOperation;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.xml.sax.InputSource;

public abstract class AbstractDBUnitTestCase {
	/**
	 * Junit4相关
	 * 
	 * @AfterClass(销毁前执行这个,用于释放资源)
	 * @BeforeClass(加载完执行这个,用于初始化资源)
	 * 
	 *                               只要是用于测试静态方法的时候用
	 *                               这个是指我们class文件载入和销毁的时候执行的方法,静态初始化
	 *                               ,是运行时类的定义,是加载完运行时类,运行时类销毁前
	 * 
	 * @After
	 * @Berfore 是指我们类的对象创建和销毁的时候执行的方法,是对象的一个定义,对象创建完成后立即执行的方法,对象销毁前执行的方法
	 * 
	 *          Junt3中 setUp(),setDown(), 这两个方法表示对象初始化和销毁的时候执行的方法
	 * 
	 */
	/**
	 * 既然是测试,肯定要数据库连接,
	 */
	// 1 连接数据库,DBUnit会代理测试中,所有的数据库连接,DBUnit是基于junit的一个扩展
	public static IDatabaseConnection connection;

	@BeforeClass
	public static void init() {
		try {
			connection = new DatabaseConnection(DBUtil.getConnection());
		} catch (DatabaseUnitException e) {
			e.printStackTrace();
		}
	}

	@AfterClass
	public static void destroy() {
		if (connection != null) {
			try {
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 2 备份数据 准备测试文件(模拟数据 t_user.xml)
	 * 
	 * 但是我们的这个文件在硬盘,我们得把它载入到内存,
	 */

	/**
	 * XML相关的知识 :
	 * 
	 * XML最初的目的是为了取代HTML而设计的标签语言,但是后来没有成功
	 * 
	 * XML 有个特点 : 所有的标签都是我们自定义,为什么自定义的标签就能用?
	 * 
	 * 所以我们需要为xml指明解析标准(XML引擎是任何软件通用的引擎,但是只有引擎,没有解析的标准)
	 * 
	 * dtd/scahme : 它们也是XML文件,里面是对我们的标签解析的一个说明
	 * 
	 * 还有一个xsl : 为了取代css , 也是一个xml
	 */
	/**
	 * 2 读取模拟测试数据文件,把xml文件中的数据按照一定的标准读入到内存中,标准是什么? 标准就是 :
	 * 这个xml文件,如果没有dtd/scahme文件的话,就会以第一条数据位标准,往数据库里面写
	 * 
	 * 在读取数据库的时候,也会以数据库中的第一条数据位标准,写入到我们xml备份文件中,这里的备份和上面的文件不是同一个
	 * 
	 * 第一个xml文件 : 是因为我们数据库里面没有数据,我们准备的模拟数据
	 * 
	 * 第二个xml文件 : 是我们数据库里面有数据,需要把数据备份,而做的一个临时文件 两者没有任何关系 但是如果我们有dtd和scahme
	 * 文件的话,这个就是解析的标准,就不会以第一条数据位标准了
	 */

	// 保存的是用具体解析我们下面的XML的一个标准文件,就是它定义了表结构
	private File dtdFile;

	// 准备好测试数据的Set
	protected IDataSet createDataSet(String t_name) {
		// 把文件载入到内存的,只能是流
		// 怎么确定当前程序执行的空间,这个上下文空间
		// 这个空间包括什么 : 运行时类,所在栈内存的空间,在堆内存的对象空间,静态数据区里面的静态数据空间
		// 通过当前类的运行时类,能够找到所有的内存空间
		// 运行时类里面肯定有一个把硬盘中的加载到我们的内存里面来,叫类加载器ClassLoader
		// getResourceAsStream() 把资源转换为流

		InputStream inputStream = AbstractDBUnitTestCase.class.getClassLoader()
				.getResourceAsStream(t_name + ".xml");

		// assertNotNull 如果这个对象不存在,为null的话,就怎么样
		Assert.assertNotNull("dbunit需要的虚拟测试数据文件不存在", inputStream);
		try {
			// inputStream : 是一个XML文件流
			// InputSource : 输入资源,把XML流找过来给了它
			// FlatXmlProducer XML解析
			// FlatXmlDataSet 转为数据集合
			return new FlatXmlDataSet(new FlatXmlProducer(new InputSource(
					inputStream)));
			// 我们直接用上面一种 以数据第一条为标准来读取所有的数据
			// return new FlatXmlDataSet(new FlatXmlProducer(new InputSource(
			// inputStream), new FlatDtdDataSet(new FileReader(dtdFile))));
		} catch (DataSetException e) {
			e.printStackTrace();
		}
		return null;
	}

	// 数据中数据保存的那个临时文件,里面不光保存了数据,也保存了表结构
	private File tempFile;

	// 3 备份数据库
	private void createBackupFile(IDataSet dataSet) {
		try {
			// createTempFile 两个参数 第一个是前缀,第二个是后缀
			//tempFile = new File("f:/testUser.xml");
			tempFile = File.createTempFile("back", "xml");
			// dtdFile = File.createTempFile("back", "xml");
			// FlatDtdDataSet.write(dataSet, new FileWriter(dtdFile));
			FlatXmlDataSet.write(dataSet, new FileWriter(tempFile));
		} catch (IOException | DataSetException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 读的话 有三种可能 : 1 所有表 , 2 N个表 , 3 一个表
	 */
	// 备份所有表
	protected void backupAllTable() {
		try {
			IDataSet dataSet = connection.createDataSet();
			createBackupFile(dataSet);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	// 备份数据库中指定的N张表
	protected void backcustomTable(String[] t_name) {
		QueryDataSet dataSet = new QueryDataSet(connection);
		try {
			for (String tname : t_name) {
				dataSet.addTable(tname);
				createBackupFile(dataSet);
			}
		} catch (AmbiguousTableNameException e) {
			e.printStackTrace();
		}
	}

	// 备份数据库中指定的一张表
	protected void backOneTable(String t_name) {
		backcustomTable(new String[] { t_name });
	}

	// 恢复数据库,清空测试数据,插入回原数据
	protected void resumeDataTable() {
		try {
			IDataSet dataSet = new FlatXmlDataSet(new FlatXmlProducer(
					new InputSource(new FileInputStream(tempFile))));
			// 有dtd的时候
			// IDataSet dataSet = new FlatXmlDataSet( new FlatXmlProducer(
			// new InputSource(new FileInputStream(tempFile)),new
			// FlatDtdDataSet(new FileReader(dtdFile))));
			// CLEAN_INSERT 清空插入
			DatabaseOperation.CLEAN_INSERT.execute(connection, dataSet);
		} catch (IOException | DatabaseUnitException | SQLException e) {
			e.printStackTrace();
		}
	}
}
