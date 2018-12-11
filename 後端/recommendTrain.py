# -*- coding: utf-8 -*-
"""
Created on Wed Oct 10 09:37:18 2018

@author: hduser
"""
from pyspark import SparkContext as sc
from pyspark import SparkConf
#import random
#from pyspark.mllib.recommendation import Rating
from pymongo import MongoClient
client = MongoClient('localhost', 27017)
db = client['test-database']#[database_name]
shops = db.rate
users = db.user
import os
os.environ["JAVA_HOME"] = "/usr/lib/jvm/java-7-openjdk-amd64"

os.environ['SPARK_HOME']='/usr/local/spark'

import findspark as fs
fs.init()

"""
shopname=range(1,46)
uid=["asxx20024","zzzz2222","x4452aa","qwe12124","asdasd21212","ghjrr21","aaaaxxxc1","aggggv5","ffffgs2","tsxa1865"]
for id in uid:
    users.insert_one({"no":users.count()+1,"uid":id,"password":uid[random.randint(0,9)]})


for user in users.find({}):
    rndshop=random.sample(shopname,10)
    for i in rndshop:
        users.update_one({'_id': user['_id']}, {'$addToSet': {'review':{'no':i,'rating':round(random.uniform(1,5),1)}}},upsert=True)
"""
with open('userData.data','w')as f:
    for user in users.find({}):
        for i in range(0,len(user['review'])):
            data = str(user['no'])+"\t"+str(user['review'][i]['no'])+"\t"+str(user['review'][i]['rating'])+"\n"
            f.write(data)
            




conf = SparkConf().setAppName("test").set("spark.ui.showConsoleProgress","false")
#sc = SparkContext(conf=conf) 
#sc=SparkContext("local"))
sc = sc.getOrCreate(conf)
rawUseData=sc.textFile("userData.data")
rawRatings=rawUseData.map(lambda line:line.split("\t")[:3])
ratingsRDD=rawRatings.map(lambda x: (x[0],x[1],x[2]))
numUSers=ratingsRDD.map(lambda x:x[0]).distinct().count()
from pyspark.mllib.recommendation import ALS
model = ALS.train(ratingsRDD,10,10,0.01)
try:
    
    r = db.recommend
    db.drop_collection("recommend")
    for i in range(1,numUSers+1):
        rmodel=model.recommendProducts(i,10)
        for m in rmodel:
            for j in shops.find({"no":m[1]},{"_id": 0, "name": 1,'formatted_address':1,'geometry':1,'rating':1}):
                    r.update_one({'no': i}, {'$addToSet': {'review':{'name':j['name'],
                                                                     'formatted_address':j['formatted_address'],
                                                                     'lat':j['geometry']['location']['lat'],
                                                                     'lng':j['geometry']['location']['lng'],
                                                                     'rating':j['rating'],
                                                                     'no':m[1]
                                                                    }}},upsert=True)
                                                                         
    print("Save OK!\n")
    
except Exception:
    print"please delete ALSmodel file\n"
    
  
