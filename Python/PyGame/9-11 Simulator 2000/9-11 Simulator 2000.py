import pygame
import random
import Elementos
from Elementos import Enemigo

pygame.init()

pygame.display.set_caption('9/11 Simulator 2000')

pantalla = pygame.display.set_mode((800, 600))
clock = pygame.time.Clock()
FPS = 30

# Elementos
fondo = Elementos.Fondo()
avion = Elementos.Avion((30,200))

# Grupo de sprites
grupo_sprites_todos = pygame.sprite.Group(fondo, avion)
grupo_sprites_enemigos = pygame.sprite.Group()
grupo_sprites_proyectiles = pygame.sprite.Group()

salir = False
tiempo = 0
tiempo_ultimo_enemigo = 0

while not salir:
    tiempo += clock.get_time()
    clock.tick(FPS)
    # Gestionar eventos
    for event in pygame.event.get():
        if event.type == pygame.QUIT:
            salir = True

    teclas = pygame.key.get_pressed()

    # Generación de enemigos
    tiempo_ultimo_enemigo += clock.get_time()

    if tiempo_ultimo_enemigo > 2000:
        nuevo_enemigo = Enemigo((900, random.randint(0, pantalla.get_height() - 50)))
        grupo_sprites_todos.add(nuevo_enemigo)
        grupo_sprites_enemigos.add(nuevo_enemigo)
        tiempo_ultimo_enemigo = 0

    # Gestionar cambios
    # pantalla.fill((0,0,0)) # Relleno de fondo, se puede quitar si ya hay fondo

    grupo_sprites_todos.update(teclas, grupo_sprites_todos, grupo_sprites_proyectiles)
    grupo_sprites_todos.draw(pantalla)

    # Redibujar el juego
    pygame.display.flip()

pygame.quit()