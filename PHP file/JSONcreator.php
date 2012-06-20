<?php
	mysql_connect("mysql hostname","mysql username","password");
	mysql_select_db("database name");
	$sql=mysql_query("select * from fakulti");
	
	$fakultiarray = array();
	if(mysql_num_rows($sql)) {
		while($fakulti = mysql_fetch_assoc($sql)) {
			$fakultiarray[] = $fakulti;
		}
	}
	
	header('Content-type: application/json');
    echo json_encode(array('fakulti'=>$fakultiarray));	//array name for parsing
  
?>