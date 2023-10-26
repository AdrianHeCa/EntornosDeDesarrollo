num = 0
sumaNums = 0
cantidadNums = 0
while num != -1:
    num = float(input('Introduce un número: '))
    if num != -1 and num > -1:
        cantidadNums += 1
        sumaNums += num
    else:
        print('Inserte un número válido')

resultado = sumaNums/cantidadNums
print(f"La mediana aritmética es {resultado}")