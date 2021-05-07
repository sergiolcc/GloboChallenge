from django.shortcuts import render
from django.http import HttpResponse
import requests
import json 

#Rota que retorna os personagens de determinado filme
def get_character(id):
    character_passed = []
    movie_character = []
    characters = requests.get(f'https://ghibliapi.herokuapp.com/people')
    for character in json.loads(characters.text):
        if character["name"] not in character_passed:
            if character["films"][0] == f'https://ghibliapi.herokuapp.com/films/{id}':
                movie_character.append(character["name"])
                character_passed.append(character["name"])
 
    movie_character_converted = ", ".join(movie_character)
   
    return  movie_character_converted if movie_character_converted else "no characters found"
#Rota que renderiza a página principal
def home(request):
    
    return render(request, "moviefilter/videolist.html")

#Rota que renderiza a página de contatos
def contact(request):
    
    return render(request, "moviefilter/contact.html")

#Rota que renderiza a página Sobre
def about(request):
    
    return render(request, "moviefilter/about.html")

#Rota que retorna todas as informações da tabela
def getdata(request):
    dados = []
    movie_data = requests.get(f'https://ghibliapi.herokuapp.com/films')
    if (movie_data):
        for movie in json.loads(movie_data.text):

            characters = get_character(movie["id"])
            
            dados.append({
                'title': movie["title"],
                'description': movie["description"],
                'director': (movie["director"]),
                'release_date': movie["release_date"],
                'score': movie["rt_score"],
                'people': characters}
                )
       
    
    json_response = {"data": dados}

    return HttpResponse(json.dumps(json_response))