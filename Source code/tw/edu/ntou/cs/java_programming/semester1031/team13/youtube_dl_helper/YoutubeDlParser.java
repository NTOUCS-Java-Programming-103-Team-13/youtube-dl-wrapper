/** 
@file YoutubeDlParser.java
@brief youtube-dl Json 輸出分析程式

本來源程式碼為「youtube-dl-helper」軟體的一部份
This source code is part of "youtube-dl-helper" software
	https://github.com/NTOUCS-Java-Programming-103-Team-13/youtube-dl-helper

本來源程式碼的架構基於「通用程式來源程式碼範本」專案
This source code's structure is based on "Generic Program Source Code Templates" project
	https://github.com/Vdragon/Generic_Program_Source_Code_Templates

建議的文字編輯器設定
Recommended text editor settings
	Indentation by tab character
	Tab character width = 2 space characters

@author 林博仁(09957010) <Henry.Lin.Taiwan@gmail.com>
@author 林夏媛 <dorislin8737@gmail.com>
@copyright 
The software has been released into public domain.
*/
package tw.edu.ntou.cs.java_programming.semester1031.team13.youtube_dl_helper;
/* Imported Java packages */
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.json.*;

/**
@brief YoutubeDlParser 類別
*/
public class YoutubeDlParser{
	/* 類別變數
	   Class fields */
	
	/* Class constructors and finalizers */
	/**
		@brief YoutubeDlParser 類別的 constructor
	*/
	private YoutubeDlParser(){
		return;
	}
	
	/* 類別方法
	   Class methods */
	/**
	 * @brief 呼叫 youtube-dl 取得媒體支援的所有格式
	 * @param url 媒體的網址
	 * @return 格式識別代碼對格式描述文字的 key-value 型態資料結構
	 */
	public static HashMap<String, String> getMediaSupportedFormats(String url){
		HashMap<String, String> formats = new HashMap<String, String>();
		Process youtube_dl_process;
		try {
			youtube_dl_process = Runtime.getRuntime().exec("youtube-dl --dump-json " + url);
			
			String line = null;
			String json_data = new String();
			InputStream youtube_dl_process_standard_output = youtube_dl_process.getInputStream();
			BufferedReader youtube_dl_output_reader = new BufferedReader (new InputStreamReader(youtube_dl_process_standard_output));
			while((line = youtube_dl_output_reader.readLine()) != null){
				json_data += line;
			}
			youtube_dl_output_reader.close();
			
			JSONObject media_json = new JSONObject(json_data);

			JSONArray format_json_array = media_json.getJSONArray("formats");
			for(int array_index = 0; array_index < format_json_array.length(); ++array_index){
				formats.put(
					format_json_array.getJSONObject(array_index).getString("format_id"), 
					format_json_array.getJSONObject(array_index).getString("format")
				);
			}
			format_json_array = null;
			media_json = null;

			youtube_dl_process.waitFor();
		} catch (IOException | InterruptedException e) {
			// TODO 自動產生的 catch 區塊
			e.printStackTrace();
		}
		return formats;
	}
	
	/**
	 * @brief 呼叫 youtube-dl 取得媒體支援的所有字幕語言的子程式
	 * @param media_url 媒體的網址
	 * @return 所有字幕語言的字串清單
	 */
	public static ArrayList<String> getMediaSupportedSubtitles(String media_url){
		ArrayList<String> subtitles = new ArrayList<String>();
		Process youtube_dl_process;
		try {
			youtube_dl_process = Runtime.getRuntime().exec("youtube-dl --all-subs --dump-json " + media_url);
			
			String line = null;
			String json_data = new String();
			InputStream youtube_dl_process_standard_output = youtube_dl_process.getInputStream();
			BufferedReader youtube_dl_output_reader = new BufferedReader (new InputStreamReader(youtube_dl_process_standard_output));
			while((line = youtube_dl_output_reader.readLine()) != null){
				json_data += line;
			}
			youtube_dl_output_reader.close();
			
			JSONObject media_json = new JSONObject(json_data);

			if(media_json.get("subtitles") != null){
				JSONObject subtitles_object = media_json.getJSONObject("subtitles");
				for(Iterator<String> i = subtitles_object.keys(); i.hasNext(); ){
					subtitles.add(i.next());
				}
			}
			
			youtube_dl_process.waitFor();
			media_json = null;
			
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
		return subtitles;
		
	}

	public static void main(String[] args){
		System.out.println("YoutubeDlParser.java unittest");
		System.out.println("－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－");
		System.out.println("有多種格式、有多種字幕語言的 Youtube 影片：");
		System.out.println(getMediaSupportedFormats("https://www.youtube.com/watch?v=iAy0tG56ZYg").toString());
		System.out.println(getMediaSupportedSubtitles("https://www.youtube.com/watch?v=iAy0tG56ZYg").toString());
		
		System.out.println("有多種格式、沒有字幕的 Youtube 影片：");
		System.out.println(getMediaSupportedFormats("https://www.youtube.com/watch?v=RCFbYAUw2QA").toString());
		System.out.println(getMediaSupportedSubtitles("https://www.youtube.com/watch?v=RCFbYAUw2QA").toString());
		return;
	}
}