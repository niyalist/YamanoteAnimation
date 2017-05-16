package net.niya.yamanote;

/*
 * 
0	train_trainnet_car_bi_id	170420081500-1
1	train_trainnet_access_datetime	2017-04-20T08:15:00.000+0900
2	train_trainnet_self_id	1
3	train_trainnet_hensei_no	東トウ501編成
4	train_trainnet_trainno	770G
5	train_trainnet_direction	内
6	train_trainnet_direction_en	Anticlockwise
7	train_trainnet_dest_code	341
8	train_trainnet_dest_station_name	品川・東京方面
9	train_trainnet_dest_station_name_en	To Shinagawa/Tokyo
10	train_trainnet_trainnet_speed	7
11	train_trainnet_ekikan_kilotei	840 こっちが現在の位置
12	train_trainnet_ekikan_kilotei_val	840
13	train_trainnet_status	8
14	train_trainnet_station_now	323
15	train_trainnet_at_train	695
16	train_trainnet_station_now_name_ja	五反田
17	train_trainnet_station_now_name_en	Gotanda
18	train_trainnet_now_latitude	35.61971156
19	train_trainnet_now_longitude	139.7284756
20	train_trainnet_now_geocode	POINT(35.61971156 139.7284756)
21	train_trainnet_station_next_cd	324
22	train_trainnet_station_next_id	319
23	train_trainnet_station_next_name_ja	大崎
24	train_trainnet_station_next_name_en	Ōsaki
25	train_train_view_flg	1
26	train_service_flg	1
27	car_trainnet_car_bi_id	170420081500-1
28	car_trainnet_access_datetime	2017/4/20 8:15
29	car_trainnet_self_id	1
30	car_trainnet_indoor_temp_car_no_1	21
31	car_trainnet_indoor_temp_car_no_2	21
32	car_trainnet_indoor_temp_car_no_3	20
33	car_trainnet_indoor_temp_car_no_4	23
34	car_trainnet_indoor_temp_car_no_5	21
35	car_trainnet_indoor_temp_car_no_6	21
36	car_trainnet_indoor_temp_car_no_7	22
37	car_trainnet_indoor_temp_car_no_8	22
38	car_trainnet_indoor_temp_car_no_9	21
39	car_trainnet_indoor_temp_car_no_10	22
40	car_trainnet_indoor_temp_car_no_11	22
41	car_trainnet_geton_rate_car_no_1	154
42	car_trainnet_geton_rate_car_no_2	168
43	car_trainnet_geton_rate_car_no_3	176
44	car_trainnet_geton_rate_car_no_4	196
45	car_trainnet_geton_rate_car_no_5	196
46	car_trainnet_geton_rate_car_no_6	208
47	car_trainnet_geton_rate_car_no_7	208
48	car_trainnet_geton_rate_car_no_8	214
49	car_trainnet_geton_rate_car_no_9	238
50	car_trainnet_geton_rate_car_no_10	228
51	car_trainnet_geton_rate_car_no_11	246
52	car_beacon_id_car_no_1	D0105444
53	car_beacon_id_car_no_2	D0105443
54	car_beacon_id_car_no_3	
55	car_beacon_id_car_no_4	D0105397
56	car_beacon_id_car_no_5	D0105445
57	car_beacon_id_car_no_6	D0105420
58	car_beacon_id_car_no_7	D0105571
59	car_beacon_id_car_no_8	D0105441
60	car_beacon_id_car_no_9	D0105430
61	car_beacon_id_car_no_10	D0105583
62	car_beacon_id_car_no_11	D0105315
 * 
 * 
 */

public class TrainStatus{
	YamanoteLineDB lineDB = YamanoteLineDB.getInstance();
	String trainID;
	YamanoteStationInfo nowStation;
	YamanoteStationInfo nextStation;
	boolean direction; //外回り(時計回り)がtrue, 内回りがfalse
	int total_kilotei;
	int current_kilotei;
	double train_angle;
	double[] rate = new double[11];
	
	TrainStatus(String line){
		String[] data = line.split(",");
		trainID = cleanVal(data[4]);
		nowStation = lineDB.stationInfo(cleanVal(data[16]));
		nextStation = lineDB.stationInfo(cleanVal(data[23]));
		direction = cleanVal(data[5]).equals("外");
		total_kilotei = Integer.parseInt(cleanVal(data[12]));
		current_kilotei = Integer.parseInt(cleanVal(data[11]));		

		double ratio = (double)current_kilotei / (double)total_kilotei;
		if(direction){
			//外回り 正方向
			train_angle = nowStation.station_angle + nowStation.angle_to_next * ratio;
		}else{
			//内回り 逆方向
			train_angle = nowStation.station_angle - nowStation.angle_to_pre * ratio;
		}

		//41-51
		for(int i = 0; i < rate.length; i++){
			double intVal = Integer.parseInt(cleanVal(data[41 + i]));
			rate[i] = (double)intVal / 100.0;
		}
	}
	
	private String cleanVal(String val){
		if(val == null){
			return "";
		}else{
			if(val.startsWith("\"")){
				String newVal = val.substring(1, val.lastIndexOf("\""));
				return newVal;
			}
			return val;
		}
	}
	
}