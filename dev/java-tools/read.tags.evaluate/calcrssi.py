
average = 0.0
length = 0.0
total = 0.0

    #Get the name of a file
filename = 'RSSI.txt'

#Open the file
infile = open(filename, 'r')

#Read the file's contents
contents = infile.read().strip().split()

#Read values from file and compute average
for line in contents:
    total += float(line)
    length = length + 1

average = total / length

#Close the file
infile.close()

#Print the amount of numbers in file and average
print('Average: ' + format(average, ',.2f'))
