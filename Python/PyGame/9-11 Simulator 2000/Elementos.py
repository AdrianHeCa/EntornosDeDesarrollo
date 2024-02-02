from typing import Any
import pygame
import math

class Fondo(pygame.sprite.Sprite):
    def __init__(self) -> None:
        super().__init__()
        pantalla = pygame.display.get_surface()
        self.image = pygame.image.load("Python/PyGame/9-11 Simulator 2000/background.jpg")
        self.image = pygame.transform.scale(self.image, (pantalla.get_width(), pantalla.get_height()))
        self.rect = self.image.get_rect()
        self.rect2 = self.image.get_rect()
        self.rect.topleft = (0, 0)
        self.rect2.topleft = (0, 0)
        self.scroll = 0

    def update(self, *args: Any, **kwargs: Any):
        self.scroll -= 5
        pantalla = pygame.display.get_surface()
        
        if self.scroll <= -self.rect.width:
            self.scroll = 0
        
        self.rect.topleft = (self.scroll, 0)
        self.rect2.topleft = (self.rect.width + self.scroll, 0)

        pantalla.blit(self.image, self.rect)
        pantalla.blit(self.image, self.rect2)


class Avion(pygame.sprite.Sprite):
    def __init__(self, posición) -> None:
        super().__init__()

        self.image = pygame.transform.scale(pygame.image.load("Python/PyGame/9-11 Simulator 2000/avion.png"),(150, 75))
        self.rect = self.image.get_rect()

        self.rect.topleft = posición
        self.amplitud = 0.6
        self.frecuencia = 0.005
        self.ultimo_disparo = 0

    def update(self, *args: Any, **kwargs: Any) -> None:
        pantalla = pygame.display.get_surface()
        teclas = args[0]
        grupo_sprites_todos = args[1]
        grupo_sprites_proyectiles = args[2]

        if teclas[pygame.K_RIGHT]:
            self.rect.x += 5
            limite = pantalla.get_width()
            self.rect.x = min(self.rect.x, limite - self.image.get_width())

        if teclas[pygame.K_LEFT]:
            self.rect.x -= 10
            self.rect.x = max(self.rect.x, 0)

        if teclas[pygame.K_UP]:
            self.rect.y -= 5
            self.rect.y = max(self.rect.y, 0)

        if teclas[pygame.K_DOWN]:
            self.rect.y += 5
            limite = pantalla.get_height()
            self.rect.y = min(self.rect.y, limite - self.image.get_height())

        if teclas[pygame.K_SPACE]:
            self.disparar(grupo_sprites_todos, grupo_sprites_proyectiles)

        self.rect.y += self.amplitud * math.sin(pygame.time.get_ticks() * self.frecuencia)

        self.rect.y = max(0, min(self.rect.y, pantalla.get_height() - self.image.get_height()))
    
    def disparar(self, grupo_sprites_todos, grupo_sprites_proyectiles):
        momento_actual = pygame.time.get_ticks()
        if momento_actual - self.ultimo_disparo > 500:
            self.ultimo_disparo = momento_actual
            nuevo_proyectil = Proyectil(self.rect.midright)
            grupo_sprites_todos.add(nuevo_proyectil)
            grupo_sprites_proyectiles.add(nuevo_proyectil)


class Enemigo(pygame.sprite.Sprite):
    def __init__(self, posición):
        super().__init__()

        self.imagenes = [
            pygame.transform.scale(pygame.image.load("Python/PyGame/9-11 Simulator 2000/helicoptero_1.png"), (80, 40)),
            pygame.transform.scale(pygame.image.load("Python/PyGame/9-11 Simulator 2000/helicoptero_2.png"), (80, 40))
        ]
        self.indice_imagen = 0
        self.image = self.imagenes[self.indice_imagen]
        self.rect = self.image.get_rect()

        self.rect.topright = posición
        self.velocidad = 10
        self.tiempo_cambio = pygame.time.get_ticks()

        self.intervalo_cambio = 25

    def update(self, *args: Any, **kwargs: Any):
        self.rect.x -= self.velocidad

        tiempo_actual = pygame.time.get_ticks()

        if tiempo_actual - self.tiempo_cambio > self.intervalo_cambio:
            self.tiempo_cambio = tiempo_actual 

            self.indice_imagen = (self.indice_imagen + 1) % len(self.imagenes)
            self.image = self.imagenes[self.indice_imagen]

        if self.rect.right < 0:
            self.kill()

        grupo_sprites_proyectiles = args[2]
        bala_collision = pygame.sprite.spritecollideany(self, grupo_sprites_proyectiles)
        if bala_collision:
            self.kill()
            bala_collision.kill()


class Proyectil(pygame.sprite.Sprite):
    def __init__(self, posición):
        super().__init__()

        self.image = pygame.Surface((10, 10))
        self.image.fill((50, 50, 50))
        self.rect = self.image.get_rect()

        self.rect.midleft = posición

        self.velocidad = 10

    def update(self, *args: Any, **kwargs: Any):
        self.rect.x += self.velocidad

        if self.rect.left > 800:
            self.kill()