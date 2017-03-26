-- 只要是和数据相关的,先执行它

-- 软件安装过程
-- 1 如果软件有数据库的,那么一定会有对应的数据库文件,里面就是建库,建表,授权语句,create 
-- 如果有示例数据的话,那么会把示例数据插入到数据库,
create table t_user{
	id int(10) primary key auto_increment,
	username varchar(32) not null unique,
}