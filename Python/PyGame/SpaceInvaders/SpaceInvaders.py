import pygame
pygame.init()

pantalla = pygame.display.set_mode((800, 600))

imagen_avion = pygame.image.load("Python/PyGame/SpaceInvaders/avion.png")
avionDer = pygame.transform.scale(imagen_avion,(90, 30))
avionIzq = pygame.transform.flip(avionDer, True, False)

salir = False

posX = 30
posY = 30
movIzq = False

while not salir:
    # Gestionar eventos
    for event in pygame.event.get():
        if event.type == pygame.QUIT:
            salir = True

    teclas = pygame.key.get_pressed()

    if teclas[pygame.K_LEFT] and posX > 0:
        posX -= 0.5
        movIzq = True
    
    if teclas[pygame.K_RIGHT] and posX < 710:
        posX += 0.5
        movIzq = False

    if teclas[pygame.K_UP] and posY > 0:
        posY -= 0.5

    if teclas[pygame.K_DOWN] and posY < 570:
        posY += 0.5

    # Gestionar cambios
    pantalla.fill((0,0,0))

    if movIzq == False:
        pantalla.blit(avionDer, (posX, posY))
    else:
        pantalla.blit(avionIzq, (posX, posY))

    # Redibujar el juego
    pygame.display.flip()

pygame.quit()