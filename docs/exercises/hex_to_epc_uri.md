# EPC decoding exercise

We received the following EPC from the reader:

- __HEX:__ `3032462080074b8000000002`

- __BIN:__ `00110000 001 100 100100011000100000100000000 00001110100101110 00000000000000000000000000000000000010`

- __Tag URI:__ `urn:epc:tag:sgtin-96:1.76300544.07470.2`

## Decoding

Steps:

1. Figuring EPC encoding through the EPC header
2. Get the filter value
3. Get partition value to get the company prefix and item reference
4. Get the serial 

### 1. Figuring EPC encoding through the EPC header

Looking at the first byte and checking it with the EPC headers snippet from TDS:

`00110000` is SGTIN-96

### 2. Get the filter value

`001` is `1` decimal tho filter value of 1

### 3. Get partition value to get the company prefix and item reference

With a partition value of `100` which is decimal `4` and consulting the SGTIN Partition Table from TDS we can conclude that:

- Company prefix has 27 bits / 8 digits
- Item reference has 17 bits / 5 digits

Converting the company prefix bits, `100100011000100000100000000`, to decimal, we get `76300544`.

Converting the item reference bits, `00001110100101110`, to decimal, we get `7470`.

### 4. Get the serial 

The serial are the remaining 38 bits to Integer:

`00000000000000000000000000000000000010` = 2 decimal

## Results

The tag URI can them be composed of:

`urn:epc:tag:sgtin-96:filter.companyPrefix.itemReference.serial`

which is:

`urn:epc:tag:sgtin-96:1.76300544.07470.2`