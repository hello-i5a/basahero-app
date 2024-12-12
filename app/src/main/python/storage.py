import numpy as np
from datetime import datetime
from supabase import create_client, Client

url = "https://zibrpedawwcozzwcepfg.supabase.co"
key = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6InppYnJwZWRhd3djb3p6d2NlcGZnIiwicm9sZSI6ImFub24iLCJpYXQiOjE3MzM4ODA4NzksImV4cCI6MjA0OTQ1Njg3OX0.lPCYMz6--EDOW_UmsfBfBu7IDcs3YKcgJrIQJ3kmMNs"
supabase: Client = create_client(url, key)


def printBucket():
    return supabase.storage.list_buckets()


def uploadImg(f, filename, title, author, genre, pub_date, description, accID):
    timestamp = datetime.now().strftime("%Y-%m-%d_%H-%M-%S-%f")
    f = np.array(f)
    img_name = filename + "_" +timestamp+".png"

    uploadImg = supabase.storage.from_("book_img").upload(
        file=f.tobytes(),
        path=img_name,
        file_options={"cache-control": "3600", "upsert": "false"})

    postBook = (
        supabase.table("books")
        .insert({"title": title, "author": author, "genre": genre, "pub_date": pub_date, "img_name": img_name, "description": description, "posted_by": accID})
        .execute()
    )
    return "Ok"

def getPostedBooksList(accID):
    response = supabase.table("books").select("book_id, title, author, genre, pub_date, rating, img_name").eq("posted_by", accID).execute()
    return response

def getPostedBook(bookID):
    response = supabase.table("books").select("title, author, genre, pub_date, rating, img_name, description").eq("book_id", bookID).execute()
    return response

def updatePostedBook(bookID, title, author, genre, pub_date, description):
    response = supabase.table("books").update({"title": title, "author": author, "genre": genre, "pub_date": pub_date, "description": description}).eq("book_id", bookID).execute()
    return response

def deleteBook(bookID, imgFilename):
    response1 = supabase.table('books').delete().eq('book_id', bookID).execute()
    response2 = supabase.storage.from_('book_img').remove([imgFilename])
    return "ok";

def getNewArrival():
    data = supabase.rpc('new_arrival').execute()
    return data

def addReadList(accID, bookID):
    data = supabase.rpc('insertbook', {"accid": accID, "bookid": bookID}).execute()
    return data

def getWantReadList(accID):
    response = supabase.table("read_list").select("books(*)").eq("user_id", accID).eq("status", 0).execute()
    return response

def getReadingList(accID):
    response = supabase.table("read_list").select("books(*)").eq("user_id", accID).eq("status", 1).execute()
    return response

def getCompletedList(accID):
    response = supabase.table("read_list").select("books(*)").eq("user_id", accID).eq("status", 2).execute()
    return response

def moveStatus(accID, bookID):
    data = supabase.rpc('move_status', {"accid": accID, "bookid": bookID}).execute()
    return data

def removeReadList(accID, bookID):
    data = supabase.rpc('remove_readlist', {"accid": accID, "bookid": bookID}).execute()
    return data

def rateBook(accID, bookID, rate):
    data = supabase.rpc('ratebook', {"accid": accID, "bookid": bookID, "rate": rate}).execute()
    return data

def updateProfile(accID, user, email, name):
    data = supabase.rpc('editprofile', {"accid": accID, "useredit": user, "emailedit": email, "nameedit": name}).execute()
    return data

def searchBook(keyword):
    data = supabase.rpc('seach_book', {"skeyword": keyword}).execute()
    return data