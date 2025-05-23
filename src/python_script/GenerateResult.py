import csv

JSONTYPE = "recursiveList"
JSONTYPE = "basicTypes"

# Remplace par le chemin de ton fichier CSV
file_path = "C:/Users/dubru/Documents/GitHub/VSPA/src/Result/result"+ JSONTYPE + ".csv"


datas = {}

with open(file_path, mode="r") as file:
    reader = csv.reader(file, delimiter=";")
    for row in reader:
        if (row[0] == "Documents ID"): continue
        datas[row[0]] = {"VSPA Time (ms)": int(row[1]) // 1000, 
                         "VSPA Memory": int(row[2]), 
                         "VSPA Result": bool(row[3])}
        
# Remplace par le chemin de ton fichier CSV
file_path = "D:/TFE2-code-gaetan/ValidatingJSONDocumentsWithLearnedVPA/schemas/benchmarks/"+ JSONTYPE + "/Results/Validation/"+ JSONTYPE + ".json-"+ JSONTYPE + ".json-0.dot-1-validation.csv"

with open(file_path, mode="r") as file:
    reader = csv.reader(file, delimiter="," if JSONTYPE=="recursiveList" else ';')
    for row in reader:
        if (row[0] == "Document ID"): continue
        datas[row[0]]["VPA Time (ms)"] = int(row[3])
        datas[row[0]]["VPA Memory"] = int(row[4])
        datas[row[0]]["VPA Result"] = bool(row[5])
        
        datas[row[0]]["JSON Time (ms)"] = int(row[-3])
        datas[row[0]]["JSON Memory"] = int(row[-2])
        datas[row[0]]["JSON Result"] = bool(row[-1])
        
        datas[row[0]]["Document Length"] = int(row[1])
        datas[row[0]]["Document Depth"] = int(row[2])
        

max_VSPA_time = 0
mean_VSPA_time = 0
max_VSPA_mem = 0
mean_VSPA_mem = 0

max_VPA_time = 0
mean_VPA_time = 0
max_VPA_mem = 0
mean_VPA_mem = 0

max_JSON_time = 0
mean_JSON_time = 0
max_JSON_mem = 0
mean_JSON_mem = 0

tot = 0

for (k,v) in datas.items():
    if len(v) > 3:
        if (v["VSPA Result"] != v["VPA Result"] or v["VSPA Result"] != v["JSON Result"]):
            print(k, " : ", v)
        max_VSPA_time = max(max_VSPA_time, v["VSPA Time (ms)"])
        max_VPA_time = max(max_VPA_time, v["VPA Time (ms)"])
        max_JSON_time = max(max_JSON_time, v["JSON Time (ms)"])
        
        max_VSPA_mem = max(max_VSPA_mem, v["VSPA Memory"])
        max_VPA_mem = max(max_VPA_mem, v["VPA Memory"])
        max_JSON_mem = max(max_JSON_mem, v["JSON Memory"])
        
        mean_VSPA_time = v["VSPA Time (ms)"]
        mean_VPA_time = v["VPA Time (ms)"]  
        mean_JSON_time = v["JSON Time (ms)"]
        
        mean_VSPA_mem = v["VSPA Memory"]
        mean_VPA_mem = v["VPA Memory"]
        mean_JSON_mem = v["JSON Memory"]
        
        tot += 1
        
        if v["VSPA Time (ms)"] > 0:
            print(k, v)

print("Max VSPA Time : \t", max_VSPA_time)
print("Max VPA Time : \t\t", max_VPA_time)
print("Max JSON Time : \t", max_JSON_time)
print()
        
print("Mean VSPA Time : \t", mean_VSPA_time / tot)
print("Mean VPA Time : \t", mean_VPA_time / tot)
print("Mean JSON Time : \t", mean_JSON_time / tot)
print()

print("Max VSPA Memory : \t", max_VSPA_mem)
print("Max VPA Memory : \t", max_VPA_mem)
print("Max JSON Memory : \t", max_JSON_mem)
print()
        
print("Mean VSPA Memory : \t", mean_VSPA_mem / tot)
print("Mean VPA Memory : \t", mean_VPA_mem / tot)
print("Mean JSON Memory : \t", mean_JSON_mem / tot)
print()