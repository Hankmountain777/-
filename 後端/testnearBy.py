# -*- coding: utf-8 -*-
# encoding=utf8  
import sys  
reload(sys)  
sys.setdefaultencoding('utf8')
import googlemaps
from pymongo import MongoClient


#搜尋方圓幾公尺的程式
#rad = input("您可以接受方圓幾公尺的店家?")        
gmaps = googlemaps.Client(key='AIzaSyAg8l0BSgDCLkYhyDiEFnzcnVzek9LAlwk')

nearby_results = gmaps.places_nearby(location = (24.149700, 120.664332), radius = 500, type = 'restaurant')

client = MongoClient('localhost', 27017)
db = client['test-database']#[database_name]
shops = db.rate#[shops is a name of collection]



nearby_results = nearby_results['results']

for nearby_result in nearby_results:
    place_id = nearby_result['place_id']
    if not(shops.find_one({"place_id":place_id})):
        detail_results = gmaps.place(place_id, language = "zh-tw")['result']
        detail_results['no']=shops.count()+1
        try:
            detail_results['rating']
        except:
            detail_results['rating']=0
        try:
            detail_results['formatted_phone_number']
        except:
            detail_results['formatted_phone_number']="No information"
        try:
            detail_results['opening_hours']
        except:
            detail_results['opening_hours']="No information"
        shops.insert_one(detail_results)#insert to Mongodb
    
#for post in shops.find():
#    print ("店名: "+post["result"]["name"]+"\n評分: "+str(post["result"]["rating"])+"\n電話: "+post['result']['formatted_phone_number']+"\n地址: "+post["result"]["formatted_address"]+"\n----------\n")

print"ok"
 





    








