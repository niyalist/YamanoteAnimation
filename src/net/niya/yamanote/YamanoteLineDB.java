package net.niya.yamanote;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class YamanoteLineDB {
	private double[] angle_to_next = {7.976676385,11.33527697,13.64431487,15.00874636,23.51020408,21.41107872,9.026239067,11.86005831,15.5335277,16.16326531,15.00874636,15.11370262,7.976676385,11.96501458,14.48396501,8.711370262,12.27988338,19.62682216,11.96501458,8.816326531,15.21865889,7.66180758,6.402332362,9.655976676,11.44023324,7.137026239,10.39067055,7.346938776,13.32944606};
	private double[] angle_to_pre = {13.32944606, 7.976676385,11.33527697,13.64431487,15.00874636,23.51020408,21.41107872,9.026239067,11.86005831,15.5335277,16.16326531,15.00874636,15.11370262,7.976676385,11.96501458,14.48396501,8.711370262,12.27988338,19.62682216,11.96501458,8.816326531,15.21865889,7.66180758,6.402332362,9.655976676,11.44023324,7.137026239,10.39067055,7.346938776};
	private String[] station_name = {"東京","有楽町","新橋","浜松町","田町","品川","大崎","五反田","目黒","恵比寿","渋谷","原宿","代々木","新宿","新大久保","高田馬場","目白","池袋","大塚","巣鴨","駒込","田端","西日暮里","日暮里","鶯谷","上野","御徒町","秋葉原","神田"};
	private double[] station_angle = {0,7.976676385,19.31195335,32.95626822,47.96501458,71.47521866,92.88629738,101.9125364,113.7725948,129.3061224,145.4693878,160.4781341,175.5918367,183.5685131,195.5335277,210.0174927,218.728863,231.0087464,250.6355685,262.6005831,271.4169096,286.6355685,294.2973761,300.6997085,310.3556851,321.7959184,328.9329446,339.3236152,346.6705539}; 
	private String[] station_name_e = {"Tokyo","Yūrakuchō","Shimbashi","Hamamatsuchō","Tamachi","Shinagawa","Ōsaki","Gotanda","Meguro","Ebisu","Shibuya","Harajuku","Yoyogi","Shinjuku","Shin-Ōkubo","Takadanobaba","Mejiro","Ikebukuro","Ōtsuka","Sugamo","Komagome","Tabata","Nishi-Nippori","Nippori","Uguisudani","Ueno","Okachimachi","Akihabara","Kanda"};
	
	private static final YamanoteLineDB instance = new YamanoteLineDB();
	
	public List<YamanoteStationInfo> lineArray = new ArrayList<>();
	private Map<String, YamanoteStationInfo> stationMap = new HashMap<>();
	
	private YamanoteLineDB() {
		for(int i = 0; i < 29; i++){
			YamanoteStationInfo info = new YamanoteStationInfo();
			info.no = i;
			info.angle_to_next = angle_to_next[i];
			info.angle_to_pre = angle_to_pre[i];
			info.station_name = station_name[i];
			info.station_name_e = station_name_e[i];
			info.station_angle = station_angle[i];
			lineArray.add(info);
			stationMap.put(info.station_name, info);
		}
	}
	public static YamanoteLineDB getInstance(){
		return instance;
	}
	
	public YamanoteStationInfo stationInfo(String stationName){
		return stationMap.get(stationName);
	}
}
