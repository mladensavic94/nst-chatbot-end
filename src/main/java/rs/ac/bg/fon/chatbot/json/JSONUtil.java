/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rs.ac.bg.fon.chatbot.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;



/**
 *
 * @author Mladen
 */
public class JSONUtil {
    
   private static final Gson gson;
   
   static{
      GsonBuilder builder = new GsonBuilder();
      gson = builder.create();
   }
   
   public static String serialize(Object obj){
       return gson.toJson(obj);
   }
   public static <T extends Object> T deserialize(String json, Class<T> type){
       return gson.fromJson(json, type);
   }
    
}
