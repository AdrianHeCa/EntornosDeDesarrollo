dia = int(input('Intoduzca el día: '))
mes = int(input('Intoduzca el mes: '))
if 0 < dia <= 30 and 0 < mes <= 12:
    print('Fecha correcta')
else:
    print('Fecha incorrecta')