"""
This module tries to print JSON from HTTP GET/POST method.
"""

#
# To test the application run application with:
#
# python ./webpy_json.py
#
# and type following URI in browser addres bar:
#
# http://localhost:8080/json/{"name": "Joe"}
#

import web
import json
from bson.json_util import dumps
URLS = (
    '/', 'HandleIndex',
    '/range/(.+)', 'rangeRating',
    '/login/','Login',
    '/sign/','Sign',
    '/top10','Top10',
    '/start','startinf',
    '/recommend/(.+)','R',
    '/rating/',"Rating",
    '/near/','near',
)

# Only for testing purpose. Do not use in production!
LAST_JSON_DOC = 'No JSON document.'

class near:    
    def GET(self):
        userdata = web.input()
        return nearSearch(userdata.lat,userdata.lng)
        
class HandleIndex(object):
    """
    Application handling index
    """

    def __init__(self):
        """
        Constructor of HandleIndex
        """
        print "Starting index app ..."

    def __del__(self):
        """
        Destructor of HandleIndex
        """
        print "Ending index app ..."

    def GET(self):
        """
        Callback method handling HTTP GET request
        """
        return "Hello"

class rangeRating(object):
    """
    Aplication handling JSON
    """

    def __init__(self):
        """
        Constructor of HandleJSON
        """
        print "Starting JSON app ..."

    def __del__(self):
        """
        Destructor of HandleIndex
        """
        print "Ending JSON app ..."

    def GET(self, res):
        """
        Callback method handling HTTP GET request
        """
        
        return searchSort(float(res))

    def POST(self, res):
        """
        Callback method handling HTTP POST request
        """
        global LAST_JSON_DOC
        json_data = web.data()
        LAST_JSON_DOC = json.loads(json_data)
        return LAST_JSON_DOC       
class Sign:    
    def GET(self):
        userdata = web.input()
        return sign(userdata.id,userdata.pwd)

class Login:    
    def GET(self):
        userdata = web.input()
        return login(userdata.id,userdata.pwd)        
        

class Top10:
    def GET(self):
        
        return top10()

class startinf:    
    def GET(self):
        return startInf() 
        
class R:
    def GET(self,res):
        
        return recommend(res)        
class Rating:
    def GET(self):
        userdata = web.input()
        rating(userdata.id,userdata.no,userdata.rating)
        return "OK"


def main():
    """
    Main function starting app
    """
    http_app = web.application(URLS, globals())
    http_app.run()
def sign(uid,pwd):
    from pymongo import MongoClient
    client = MongoClient('localhost', 27017)
    db = client['test-database']#[database_name]
    users = db.user
    if users.find_one({'uid':uid}):
        sta="NO"
    else:
        inf={'uid':uid,'password':pwd,'no':users.count()+1}
        users.insert_one(inf)
        sta="OK"
    return sta

def login(uid,pwd):
    from pymongo import MongoClient
    client = MongoClient('localhost', 27017)
    db = client['test-database']#[database_name]
    users = db.user
    if (users.find_one({'uid':uid})):
        if (users.find_one({'uid':uid,'password':pwd})):
            return "SUCCESS"
        else:
            return "password error"
    else:
        return "Never sign"

def searchSort(r):
    from pymongo import MongoClient
    client = MongoClient('localhost', 27017)
    db = client['test-database']#[database_name]
    shops = db.rate
    c = dumps(shops.find({'rating':{"$gte":r}},{"_id": 0, "name": 1,'rating':1,'formatted_address':1,'website':1,'no':1}).sort('rating',-1))
    return c

def startInf():
    from pymongo import MongoClient
    client = MongoClient('localhost', 27017)
    db = client['test-database']#[database_name]
    shops = db.rate
    c = dumps(shops.find({},{"_id": 0, "name": 1,'rating':1,'formatted_address':1,'website':1,'no':1}).limit(20))
    return c    

def recommend(u):
    from bson.json_util import dumps
    from pymongo import MongoClient
    client = MongoClient('localhost', 27017)
    db = client['test-database']#[database_name]
    r = db.recommend
    users = db.user
    for n in users.find({"uid":u}):
        no = n["no"]
    for d in r.find({"no":no}):
        c=dumps(d['review'])
    return c
    
def rating(uid,no,ratingscore):
    from pymongo import MongoClient
    client = MongoClient('localhost', 27017)
    db = client['test-database']#[database_name]
    user=db.user
    user.update_one({'uid':uid},{'$pull':{'review':{'no':no}}},upsert=True)
    user.update_one({'uid':uid}, {'$push':{'review':{'no':no,"rating":ratingscore}}},upsert=True)

def top10():
    from pymongo import MongoClient
    client = MongoClient('localhost', 27017)
    db = client['test-database']#[database_name]
    shops = db.rate
    c = dumps(shops.find({},{"_id": 0, "name": 1,'rating':1,'formatted_address':1,"no":1}).sort('rating',-1).limit(10))
    return c

def nearSearch(lat,lng):  
    import googlemaps
    from pymongo import MongoClient
    gmaps = googlemaps.Client(key='AIzaSyAg8l0BSgDCLkYhyDiEFnzcnVzek9LAlwk')

    nearby_results = gmaps.places_nearby(location = (lat, lng), radius = 300, type = 'restaurant')

    client = MongoClient('localhost', 27017)
    db = client['test-database']#[database_name]
    shops = db.rate#[shops is a name of collection]
    nearinf=db.nearinfs    
    db.drop_collection("nearinfs")
    nearby_results = nearby_results['results']

    for nearby_result in nearby_results:
        place_id = nearby_result['place_id']
        detail_results = gmaps.place(place_id, language = "zh-tw")['result']
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
        if not(shops.find_one({"place_id":place_id})):
            detail_results['no']=shops.count()+1
            shops.insert_one(detail_results)
        else:
            nearinf.insert_one(detail_results)
            
    c = dumps(nearinf.find({},{"_id": 0, "name": 1,'rating':1,'formatted_address':1}).sort('rating',-1))
    return c
    

if __name__ == "__main__":
    main()