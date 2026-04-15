import re

with open("data_wolfe.json", "r") as f:
    text = f.read()
    f.close()

# r = raw string
# \ = escape character
# () = capture inside
# . = match any character
# * = keep taking characters
# ? = stop as soon as you see ___
# for refactoring, just change the word 'faculty' and it should hypothetically work
pattern = r"\"faculty\":\[(.*?)\]"
matches = re.findall(pattern, text, re.S)
matches2 = []

#gets rid of most duplicates
for i in range(len(matches)):
    match = matches[i]
    if matches[i] not in matches2:
        matches2.append(matches[i])

#need to save m + "," to faculty output
with open("faculty_output.txt", "w") as file1:
    for m in matches2:
        file1.write(m + ",\n")
    file1.close()

#need to make it automatically adjust the stuff with commas
with open("faculty_output.txt", "r") as f1:
    nexttext = f1.read()
    result = nexttext.replace('","', '",\n"')
    f1.close()

#saving result
with open("faculty_output.txt", "w") as f2:
        for r in result:
            f2.write(r)
        f2.close()

matches3 = []
matches4 = []

#getting rid of the rest of the duplicates
with open("faculty_output.txt", "r") as g:
    for line in g:              
        line = line.strip()
        matches3.append(line)
    g.close()

for item in matches3:
    if item not in matches4:
        matches4.append(item)

#removing all "" and , and saving matches4 to faculty_output file
with open("faculty_output.txt", "w") as file:
    for m in matches4:
        m = m.lstrip('"')
        m = m.rstrip('",')
        file.write(m + "\n")
    file.close()

#sorting alphabetically
with open("faculty_output.txt", "r") as g2:
    text2 = []  
    for line in g2:
        if line.strip():
            text2.append(line)
    text2.sort()

with open("faculty_output.txt", "w") as g3:
    for line in text2:
        g3.write(line)
    g3.close()

print("Task completed!")