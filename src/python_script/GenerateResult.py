import csv
import random

JSONTYPE = "recursiveList"
JSONTYPE = "basicTypes"
JSONTYPE = "proxies"
JSONTYPE = "vim"

# Remplace par le chemin de ton fichier CSV
time_vspa_path2 =  "C:/Users/dubru/Documents/GitHub/VSPA/src/Result/result"+ JSONTYPE + ".csv"
time_vspa_path =  "C:/Users/dubru/Documents/GitHub/VSPA/src/Result/time-result"+ JSONTYPE + ".csv"
memo_vspa_path =  "C:/Users/dubru/Documents/GitHub/VSPA/src/Result/memory-result"+ JSONTYPE + ".csv"

time_vpa_path =  "D:/TFE2-code-gaetan/ValidatingJSONDocumentsWithLearnedVPA/schemas/benchmarks/"+ JSONTYPE + "/Results/Validation/"+ JSONTYPE + ".json-"+ JSONTYPE + ".json-0.dot-1-validation-TIME.csv"
memo_vpa_path =  "D:/TFE2-code-gaetan/ValidatingJSONDocumentsWithLearnedVPA/schemas/benchmarks/"+ JSONTYPE + "/Results/Validation/"+ JSONTYPE + ".json-"+ JSONTYPE + ".json-0.dot-1-validation-MEMORY.csv"

if JSONTYPE=="vim":
    memo_vpa_path = f"D:/TFE2-code-gaetan/ValidatingJSONDocumentsWithLearnedVPA/schemas/benchmarks/{JSONTYPE}/Results/Validation/vim-addon-info.json-vim-addon-info.json-0.dot-1-validation-MEMORY.csv"



datas = {}
t = 0
with open(time_vspa_path, mode="r") as file:
    reader = csv.reader(file, delimiter=";")
    for row in reader:
        if row[0] == "Documents ID": continue
        datas[row[0]] = {"VSPA Time (ms)": int(row[1]), 
                         "VSPA Memory": -1, 
                         "VSPA Result": True if row[3]=="true" else False}
        t += 1 if row[3]=="true" else 0
        
with open(time_vspa_path2, mode="r") as file:
    reader = csv.reader(file, delimiter=";")
    for row in reader:
        if row[0] == "Documents ID": continue
        if not row[0] in datas: continue
        datas[row[0]]["VSPA Time (ms)2"] = int(row[1])
        datas[row[0]]["VSPA Result2"] = True if row[3]=="true" else False
        assert datas[row[0]]["VSPA Result"] == datas[row[0]]["VSPA Result2"]
        t += 1 if row[3]=="true" else 0
        


with open(memo_vspa_path, mode="r") as file:
    reader = csv.reader(file, delimiter=";")
    for row in reader:
        if row[0] == "Documents ID": continue
        
        if datas[row[0]]["VSPA Memory"] != -1:
            print(row[0])
            input()
        datas[row[0]]["VSPA Memory"] = int(row[2])
        assert datas[row[0]]["VSPA Result"] == (True if row[3]=="true" else False)


if JSONTYPE=="vim" or JSONTYPE=="proxies":
    nbr_doc = 10
    for i in range(nbr_doc):
        time_vpa_path =  f"D:/TFE2-code-gaetan/ValidatingJSONDocumentsWithLearnedVPA/schemas/benchmarks/{JSONTYPE}/Results/Validation/{JSONTYPE}.json-{JSONTYPE}.json-0.dot-1-validation-TIME-{i}.csv"
        if JSONTYPE=="vim":
            time_vpa_path = f"D:/TFE2-code-gaetan/ValidatingJSONDocumentsWithLearnedVPA/schemas/benchmarks/{JSONTYPE}/Results/Validation/vim-addon-info.json-vim-addon-info.json-0.dot-1-validation-TIME-{i}.csv"
        with open(time_vpa_path, mode="r") as file:
            reader = csv.reader(file, delimiter=",")
            for row in reader:
                if not row or row[0] == "Document ID" or row[0] not in datas: continue
                
                if not "VPA Time (ms)" in datas[row[0]]:
                    datas[row[0]]["VPA Time (ms)"] = 0
                datas[row[0]]["VPA Time (ms)"] += int(row[3])/nbr_doc
                datas[row[0]]["VPA Memory"] = -1
                datas[row[0]]["VPA Result"] =  True if row[4]=="true" else False
                
                if not "JSON Time (ms)" in datas[row[0]]:
                    datas[row[0]]["JSON Time (ms)"] = 0
                datas[row[0]]["JSON Time (ms)"] += int(row[-2])/nbr_doc
                datas[row[0]]["JSON Memory"] = -1
                datas[row[0]]["JSON Result"] = True if row[-1]=="true" else False
                
                datas[row[0]]["Document Length"] = int(row[1])
                datas[row[0]]["Document Depth"] = int(row[2])
                
                assert datas[row[0]]["VSPA Result"] == datas[row[0]]["JSON Result"] == datas[row[0]]["VPA Result"]
else:
    if JSONTYPE == "basicTypes":
        with open(time_vpa_path, mode="r") as file:
            reader = csv.reader(file, delimiter=",")
            for row in reader:
                if not row or row[0] == "Document ID" or row[0] not in datas: continue
                
                datas[row[0]]["VPA Time (ms)"] = int(row[3])
                datas[row[0]]["VPA Memory"] = -1
                datas[row[0]]["VPA Result"] =  True if row[4]=="true" else False
                
                datas[row[0]]["JSON Time (ms)"] = int(row[-2])
                datas[row[0]]["JSON Memory"] = -1
                datas[row[0]]["JSON Result"] = True if row[-1]=="true" else False
                
                datas[row[0]]["Document Length"] = int(row[1])
                datas[row[0]]["Document Depth"] = int(row[2])
            
            assert datas[row[0]]["VSPA Result"] == datas[row[0]]["JSON Result"] == datas[row[0]]["VPA Result"]
    else:
        with open(time_vpa_path, mode="r") as file:
            reader = csv.reader(file, delimiter=",")
            for row in reader:
                if not row or row[0] == "Document ID" or row[0] not in datas: continue
                
                datas[row[0]]["VPA Time (ms)"] = int(row[3])
                datas[row[0]]["VPA Memory"] = -1
                datas[row[0]]["VPA Result"] =  True if row[5]=="true" else False
                
                datas[row[0]]["JSON Time (ms)"] = int(row[-3])
                datas[row[0]]["JSON Memory"] = -1
                datas[row[0]]["JSON Result"] = True if row[-1]=="true" else False
                
                datas[row[0]]["Document Length"] = int(row[1])
                datas[row[0]]["Document Depth"] = int(row[2])
            
            assert datas[row[0]]["VSPA Result"] == datas[row[0]]["JSON Result"] == datas[row[0]]["VPA Result"]
        
with open(memo_vpa_path, mode="r") as file:
    reader = csv.reader(file, delimiter=",")
    for row in reader:
        if not row or row[0] == "Document ID" or row[0] not in datas: continue
        
        datas[row[0]]["VPA Memory"] = int(row[4])
        datas[row[0]]["JSON Memory"] = int(row[-2]) + int(row[1])/20
        
        assert datas[row[0]]["JSON Result"] == (True if row[5]=="true" else False)
        assert datas[row[0]]["VPA Result"] == (True if row[-1]=="true" else False)

max_VSPA_time = 0
mean_VSPA_time = 0
mean_VSPA_time2 = 0
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
            if k == "vim-addon-info.json-valid-4685.json":
                continue
            input()
            continue
        max_VSPA_time = max(max_VSPA_time, v["VSPA Time (ms)"])
        max_VPA_time = max(max_VPA_time, v["VPA Time (ms)"])
        max_JSON_time = max(max_JSON_time, v["JSON Time (ms)"])
        
        max_VSPA_mem = max(max_VSPA_mem, v["VSPA Memory"])
        max_VPA_mem = max(max_VPA_mem, v["VPA Memory"])
        max_JSON_mem = max(max_JSON_mem, v["JSON Memory"])
        
        mean_VSPA_time += v["VSPA Time (ms)"]
        mean_VSPA_time2 += v["VSPA Time (ms)2"]
        mean_VPA_time += v["VPA Time (ms)"] 
        mean_JSON_time += v["JSON Time (ms)"] 
        
        if not (v["VSPA Memory"] == -1 or v["VPA Memory"] == -1 or v["JSON Memory"] == -1):
            mean_VSPA_mem += v["VSPA Memory"]
            mean_VPA_mem += v["VPA Memory"]
            mean_JSON_mem += v["JSON Memory"]
        
        tot +=  v["Document Length"]
        
        if v["VSPA Time (ms)"] < 0:
            print(k, v)

print("Max VSPA Time : \t", max_VSPA_time/1000)
print("Max VPA Time : \t\t", max_VPA_time)
print("Max JSON Time : \t", max_JSON_time)
print()
        
print("Mean VSPA Time : \t", mean_VSPA_time / tot)
print("Mean VSPA2 Time : \t", mean_VSPA_time2 / tot)
print("Mean VPA Time : \t", mean_VPA_time / tot *1000)
print("Mean JSON Time : \t", mean_JSON_time / tot *1000)
print()

print("Max VSPA Memory : \t", max_VSPA_mem)
print("Max VPA Memory : \t", max_VPA_mem)
print("Max JSON Memory : \t", max_JSON_mem)
print()
        
print("Mean VSPA Memory : \t", mean_VSPA_mem / tot)
print("Mean VPA Memory : \t", mean_VPA_mem / tot)
print("Mean JSON Memory : \t", mean_JSON_mem / tot)
print()

import matplotlib.pyplot as plt

VSPA_times = []
VPA_times = []
JSON_times = []

VSPA_mem = []
VPA_mem = []
JSON_mem = []

DOC_len = []
DOC_len_mem = []

for (k,v) in datas.items():
    if len(v) > 3:
        if not ((v["VSPA Time (ms)"] == max_VSPA_time)*max_VSPA_time or v["VSPA Memory"]==max_VSPA_mem or
                (v["VPA Time (ms)"] == max_VPA_time)*max_VPA_time or v["VPA Memory"]==max_VPA_mem or
                (v["JSON Time (ms)"] == max_JSON_time)*max_JSON_time or v["JSON Memory"]==max_JSON_mem):
            if JSONTYPE == "vim" or JSONTYPE == "proxies":
                if v["Document Length"] < 25000:
                    if random.random() > .1:
                        continue
                elif v["Document Length"] < 75000:
                    if random.random() > .4:
                        continue
            if JSONTYPE == "basicTypes":
                if v["Document Length"] < 1000:
                    if random.random() > .05:
                        continue
        else: 
            print(k,v)
            
        VSPA_times.append(v["VSPA Time (ms)"]/1000)
        VPA_times.append(v["VPA Time (ms)"])
        JSON_times.append(v["VSPA Time (ms)2"]/1000)
        # JSON_times.append(v["JSON Time (ms)"])
        
        if not (v["VSPA Memory"] == -1 or v["VPA Memory"] == -1 or v["JSON Memory"] == -1):
            VSPA_mem.append(v["VSPA Memory"])
            VPA_mem.append(v["VPA Memory"])
            JSON_mem.append(v["JSON Memory"])
            
            DOC_len_mem.append(v["Document Length"])
        
        DOC_len.append(v["Document Length"])

plt.figure(dpi=300)
plt.plot(DOC_len, VSPA_times, 'gs', 
         DOC_len, VPA_times, 'bx', 
         DOC_len, JSON_times, 'ro',
         markerfacecolor='none')
plt.xlabel("Document Length")
plt.ylabel("Time (ms)")
plt.legend(["VSPA", "VPA", "VSPA2"])
if JSONTYPE == "proxies":
    plt.title("Azure Proxies")
if JSONTYPE == "vim":
    plt.title("VIM Plugins")
if JSONTYPE == "recursiveList":
    plt.title("Recursive List")
if JSONTYPE == "basicTypes":
    plt.title("Basic Types")
plt.grid(True)
plt.show()

import sys
sys.exit()

plt.figure(dpi=500)
plt.plot(DOC_len_mem, VSPA_mem, 'gs', 
         DOC_len_mem, VPA_mem, 'bx', 
#         DOC_len_mem, JSON_mem, 'ro', 
         markerfacecolor='none')
plt.xlabel("Document Length")
plt.ylabel("Memory (kB)")
plt.legend(["VSPA", "VPA", "Classical"])
plt.grid(True)
if JSONTYPE == "proxies":
    plt.title("Azure Proxies")
if JSONTYPE == "vim":
    plt.title("VIM Plugins")
if JSONTYPE == "recursiveList":
    plt.title("Recursive List")
if JSONTYPE == "basicTypes":
    plt.title("Basic Types")
plt.show()

invalids = set()
valids = set()
for k,v in datas.items():
    if len(v) > 3:
        Id = int(k.split('.')[-2].split('-')[-1])
        if k.split('.')[-2].split('-')[-2] == 'invalid':
            if Id in invalids:
                print("duplicate invalid ",Id)
            invalids.add(Id)
        else:
            if Id in valids:
                print("duplicate valid ",Id)
            valids.add(Id)
            
for i in range(5000):
    if not i in invalids: 
        print(f"invalid-{i}")
    if not i in valids: 
        print(f"valid-{i}")