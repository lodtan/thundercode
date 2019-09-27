
# with open("C:/Users/tbres/Downloads/ai.meta.stackexchange.com/Posts.xml") as myfile:
#     head = []
#     if len(head) == 0:
#         head = [next(myfile) for x in range(10)]
#     else:
#         while head[len(head)] != "</posts>":
#  ²           head = [next(myfile) for x in range(10)]
#
#
# print(head)

chunked_file_line_count = 400
j = 0
with open("C:/Users/tbres/Downloads/Donnees/Posts.xml", encoding="utf8") as file_obj:
    line_count = 0
    i = 0
    file_count = 0
    chunked_file_object = open("C:/Users/tbres/Downloads/Donnees/file_"+str(file_count)+".xml", "w", encoding="utf8")
    for line in file_obj:
        if line == "<posts>\n" or i == 0 or line == "</posts>":
            print(0)
        elif i == 20000:
            break
        else:
            if line_count == 0:
                chunked_file_object.write("<posts>\n")

            chunked_file_object.write(line)
            line_count = line_count + 1
            if line_count == chunked_file_line_count:
                chunked_file_object.write("</posts>\n")
                file_count = file_count + 1
                line_count = 0
                print("writing file " + str(file_count))
                chunked_file_object = open("C:/Users/tbres/Downloads/Donnees/file_"+str(file_count)+".xml","w", encoding="utf8")

        i = i+1
    chunked_file_object.close()