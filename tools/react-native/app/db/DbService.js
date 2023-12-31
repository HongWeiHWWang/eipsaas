
import {AsyncStorage,Platform } from 'react-native';
import SQLite from 'react-native-sqlite-storage';
SQLite.DEBUG(true);
SQLite.enablePromise(false);

const database_name = "Test.db";
const database_version = "1.0";
const database_displayname = "SQLite Test Database";
const database_size = 200000;

export function open() {
	global.dbEntity = SQLite.openDatabase(database_name, database_version, database_displayname, database_size, openCB, errorCB);
}

function openCB () {
	//初始化数据库表

	//会话表
	global.dbEntity.executeSql('CREATE TABLE IF NOT EXISTS session ( '
		+ 'id_ integer(10) PRIMARY KEY, '
	    + 'session_code_ VARCHAR(100),'
	    + 'title_ VARCHAR(200), '
	    + 'last_text_ VARCHAR(200) ,'
	    + 'icon_ VARCHAR(200) ,'
	    + 'scene_ VARCHAR(10) ,'
	    + 'last_read_time_ DATETIME ,'
	    + 'create_time_ DATETIME ,'
	    + 'owner_ VARCHAR(20) ) ; ', [],(result) =>{},(err) =>{console.warn(err)});

	//会话消息表
	global.dbEntity.executeSql('CREATE TABLE IF NOT EXISTS message( '
		+ 'id_ integer(10) PRIMARY KEY, '
	    + 'message_id_ VARCHAR(100),'
	    + 'session_code_ VARCHAR(100),'
	    + 'type_ VARCHAR(20), '
	    + 'from_ VARCHAR(20), '
	    + 'content_ TEXT ,'
	    + 'send_time_ DATETIME , owner_ VARCHAR(20) ) ; ', [],(result) =>{},(err) =>{console.warn(err)});

	//global.dbEntity.executeSql('ALTER TABLE `session` ADD `last_read_time_` DATETIME', [],(result) =>{},(err) =>{console.warn(err)});
	
}

export function executeSql(sql,callback) {
	 global.dbEntity.executeSql(sql, [],(result)=>{
	 	if(callback){
	 		callback(result.rows.raw());
	 	}
	 },(err) =>{
		/*if(callback){
	 		callback(err);
	 	}*/
	 });
}


function errorCB(){
  console.warn("error db ")
}
