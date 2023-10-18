factorial = 1
i = 1

num = int(input('Intoduzca un número: '))

if num < 0:
    print('No se puede calcular el factorial de un número negativo.')
else:
    while i <= num:
        factorial = factorial * i
        i += 1

print('El factorial de ', num, ' es ', factorial)