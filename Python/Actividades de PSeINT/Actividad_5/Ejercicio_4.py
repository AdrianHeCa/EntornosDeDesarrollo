intentos = 3
contraseña = ''
while contraseña != 'eureka' and intentos != 0:
    contraseña = input('Intoduzca la contraseña: ')
    if contraseña != 'eureka':
        intentos -= 1
        print('Contraseña incorrecta, te queda(n) ', intentos, 'intento(s)')
    if contraseña == 'eureka':
        print('Contraseña correcta')
    if intentos == 0:
        print('Demasiados intentos fallidos')