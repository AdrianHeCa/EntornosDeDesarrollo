num = 0
sumaNums = 0
cantidadNums = 0
while num != -1:
    num = float(input('Introduce un número: '))
    cantidadNums += 1
    sumaNums += num

resultado = sumaNums/cantidadNums
print(f"La mediana aritmética es {resultado}")