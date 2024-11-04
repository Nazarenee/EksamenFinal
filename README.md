# README

3.3.2 :
POST {{url}}/trips/populate
output:
HTTP/1.1 200 OK
Database populated successfully.
----------------------------------------------------------------------
GET {{url}}/trips
output:
[
{
"id": 3,
"startTime": [
2021,
1,
1,
10,
0
],
"endTime": [
2021,
1,
1,
11,
0
],
"startLocation": "Beach Resort",
"name": "Best trip ever",
"price": 2000,
"category": "BEACH",
"guide": {
"id": 3,
"firstName": "Joh",
"lastName": "Doe",
"email": "JohnDoe@gmail.com",
"phoneNumber": "12345678",
"yearsOfExperience": 10.0,
"trips": []
}
},
{
"id": 4,
"startTime": [
2021,
1,
1,
10,
0
],
"endTime": [
2021,
1,
1,
11,
0
],
"startLocation": "Copehagen centrym",
"name": "Come see the great city of copenhagen!",
"price": 2000,
"category": "CITY",
"guide": {
"id": 4,
"firstName": "Lebron",
"lastName": "Flames",
"email": "LebronFlames@gmail.com",
"phoneNumber": "12345678",
"yearsOfExperience": 100.0,
"trips": []
}
},
{
"id": 1,
"startTime": [
2021,
1,
1,
10,
0
],
"endTime": [
2021,
1,
1,
11,
0
],
"startLocation": "Copehagen centrum, THE PLACE WHERE THE MAGIC HAPPENDS! ",
"name": "Come see the great city of copenhagen!",
"price": 2000,
"category": "BEACH",
"guide": {
"id": 1,
"firstName": "Joh",
"lastName": "Doe",
"email": "JohnDoe@gmail.com",
"phoneNumber": "12345678",
"yearsOfExperience": 10.0,
"trips": []
}
},
{
"id": 5,
"startTime": [
2021,
1,
1,
10,
0
],
"endTime": [
2021,
1,
1,
11,
0
],
"startLocation": "Beach Resort",
"name": "Best trip ever",
"price": 2000,
"category": "BEACH",
"guide": {
"id": 5,
"firstName": "Joh",
"lastName": "Doe",
"email": "JohnDoe@gmail.com",
"phoneNumber": "12345678",
"yearsOfExperience": 10.0,
"trips": []
}
},
{
"id": 6,
"startTime": [
2021,
1,
1,
10,
0
],
"endTime": [
2021,
1,
1,
11,
0
],
"startLocation": "Copehagen centrym",
"name": "Come see the great city of copenhagen!",
"price": 2000,
"category": "CITY",
"guide": {
"id": 6,
"firstName": "Lebron",
"lastName": "Flames",
"email": "LebronFlames@gmail.com",
"phoneNumber": "12345678",
"yearsOfExperience": 100.0,
"trips": []
}
}
]
---------------------------------------------------------------------------------------------------------------------------------------------------
GET {{url}}/trips/1
output:
{
"id": 1,
"startTime": [
2021,
1,
1,
10,
0
],
"endTime": [
2021,
1,
1,
11,
0
],
"startLocation": "Copehagen centrum, THE PLACE WHERE THE MAGIC HAPPENDS! ",
"name": "Come see the great city of copenhagen!",
"price": 2000,
"category": "BEACH",
"guide": {
"id": 1,
"firstName": "Joh",
"lastName": "Doe",
"email": "JohnDoe@gmail.com",
"phoneNumber": "12345678",
"yearsOfExperience": 10.0,
"trips": []
}
}
PUT {{url}}/trips/1
output:
{
"id": 1,
"startTime": [
2021,
1,
1,
10,
0
],
"endTime": [
2021,
1,
1,
11,
0
],
"startLocation": "Copehagen centrum, THE PLACE WHERE THE MAGIC HAPPENDS! ",
"name": "Come see the great city of copenhagen!",
"price": 2000,
"category": "BEACH",
"guide": {
"id": 1,
"firstName": "Joh",
"lastName": "Doe",
"email": "JohnDoe@gmail.com",
"phoneNumber": "12345678",
"yearsOfExperience": 10.0,
"trips": []
}
}
---------------------------------------------------------------------------------------------------------------------------------------------------
DELETE {{url}}/trips/1
output:
{
"status": 200,
"message": "Resource deleted."
}

3.3.5 : 
Ideen er at man skal kunne tilføje en guide til en eksisterende trip. DVS at man i virkeligheden, hiver fat i en eksisterende trip også opdatere man (PUT) trip'en med en guide.
Hvis man have brugt POST, ville man ende med at have 2 trips, en med guide og en uden guide. 