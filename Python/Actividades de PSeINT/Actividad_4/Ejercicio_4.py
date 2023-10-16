a = int(input('Intoduzca el valor a: '))
b = int(input('Intoduzca el valor b: '))
c = int(input('Intoduzca el valor c: '))
if a > b and a > c:
    print('a es mayor que b y c')
elif b > a and b > c:
    print('b es mayor que a y c')
elif c > a and c > b:
    print('c es mayor que a y b')
elif a == b and a == c:
    print('Todos son iguales')