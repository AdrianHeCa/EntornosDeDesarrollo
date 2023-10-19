num = 1
sumaNums = 0
cantidadNums = 0
maximo = float('-inf')
minimo = float('inf')

while True:
    num = float(input("Escriba un número: "))

    if num == 0:
        break
    
    if num > maximo:
        maximo = num
    
    if num < minimo:
        minimo = num
    
    cantidadNums += 1
    sumaNums += num

media = sumaNums / cantidadNums

print(f'La media es {media}, el mayor es {maximo} y el menor {minimo}')
