import cgi

from neo4j import GraphDatabase
import xml.etree.cElementTree as et
import pandas as pd

uri = "bolt://localhost:7687"
driver = GraphDatabase.driver(uri, auth=("thomas", "thomas"))


def print_friends_of(tx, n):
    for record in tx.run("MATCH (n:Person) RETURN n "
                         ):
        print(record["n"])


with driver.session() as session:
    session.read_transaction(print_friends_of, "Alice")
    # session.read_transaction(addPersonn)
    # session.run("CREATE (person:Person {name:'Axel'}) ")

path = 'C:/Users/tbres/Downloads/Donnees/file_0.xml'
dfcols = ['Id', 'ParentId', 'PostTypeId', 'AcceptedAnswerId', 'CreationDate', 'Score', 'ViewCount', 'Body',
          'OwnerUserId',
          'LastActivityDate', 'Title', 'Tags', 'AnswerCount', 'CommentCount']
root = et.parse(path)
rows = root.findall('.//row')

# NESTED LIST
xml_data = [
    [row.get('Id'), row.get('ParentId'), row.get('PostTypeId'), row.get('AcceptedAnswerId'), row.get('CreationDate'),
     row.get('Score'),
     row.get('ViewCount'), row.get('Body'), row.get('OwnerUserId'), row.get('LastActivityDate'),
     row.get('Title'), row.get('Tags'), row.get('AnswerCount'), row.get('CommentCount')]
    for row in rows]

df_xml = pd.DataFrame(xml_data, columns=dfcols)
DNN = df_xml
# DNN = df_xml.loc[((df_xml['AnswerCount'] != '0') | (df_xml['CommentCount'] != '0') & (df_xml['ParentId'].isnull())) |
#                  (df_xml['ParentId'].notnull())]
DNN.loc[:, 'Body'] = DNN['Body'].str.replace(r'\\', '/')
DNN.loc[:, 'Body'] = DNN['Body'].str.replace("'", "\\'")
DNN.loc[:, 'Body'] = DNN['Body'].str.replace('"', '\\"')
DNN.loc[:, 'Body'] = DNN['Body'].str.replace('\t', '\\t')
DNN.loc[:, 'Body'] = DNN['Body'].str.replace('\n', '\\n')
DNN.loc[:, 'Body'] = DNN['Body'].str.replace('\b', '\\b')
DNN.loc[:, 'Body'] = DNN['Body'].str.replace('\f', '\\f')
DNN.loc[:, 'Body'] = DNN['Body'].str.replace('\r', '\\r')

DNN.loc[:, 'Title'] = DNN['Title'].str.replace(r'\\', '/')
DNN.loc[:, 'Title'] = DNN['Title'].str.replace("'", "\\'")
DNN.loc[:, 'Title'] = DNN['Title'].str.replace('"', '\\"')
DNN.loc[:, 'Title'] = DNN['Title'].str.replace('\t', '\\t')
DNN.loc[:, 'Title'] = DNN['Title'].str.replace('\n', '\\n')
DNN.loc[:, 'Title'] = DNN['Title'].str.replace('\b', '\\b')
DNN.loc[:, 'Title'] = DNN['Title'].str.replace('\f', '\\f')
DNN.loc[:, 'Title'] = DNN['Title'].str.replace('\r', '\\r')
DNN.loc[DNN['AcceptedAnswerId'].isnull() & DNN['PostTypeId'] == 1, 'AcceptedAnswerId'] = '0'
DNN.loc[DNN['OwnerUserId'].isnull(), 'OwnerUserId'] = '0'
with driver.session() as session:
    queryDelete = "MATCH (post:Post) DETACH DELETE post"
    session.run(queryDelete)
    print("suppri")
    for i in range(0, DNN.__len__()):
        print(DNN.Id[i])
        if DNN.PostTypeId[i] == '1':
            query = "CREATE (post:Post {IdPost:" + DNN.Id[i] + "," \
                     "AcceptedAnswerId:" + DNN.AcceptedAnswerId[i] + "," \
                     "CreationDate:'" + DNN.CreationDate[i] + "'," \
                     "Score:" + DNN.Score[i] + "," \
                     "Body:'" + DNN.Body[i] + "'," \
                     "ViewCount:" + DNN.ViewCount[i] + "," \
                     "OwnerUserId:" + DNN.OwnerUserId[i] + "," \
                     "LastActivityDate:'" + DNN.LastActivityDate[i] + "'," \
                     "Title:'" + DNN.Title[i] + "'," \
                     "Tags:'" + DNN.Tags[i] + "'," \
                     "AnswerCount:" + DNN.AnswerCount[i] + "," \
                     "CommentCount:" + DNN.CommentCount[i] + "}) "
            session.run(query)
        elif DNN.PostTypeId[i] == '2':
            query = "CREATE (post:Post {IdPost:" + DNN.Id[i] + "," \
                     "ParentId:" + DNN.ParentId[i] + "," \
                     "CreationDate:'" + DNN.CreationDate[i] + "'," \
                     "Score:" + DNN.Score[i] + "," \
                     "Body:'" + DNN.Body[i] + "'," \
                     "LastActivityDate:'" + DNN.LastActivityDate[i] + "'," \
                     "CommentCount:" + DNN.CommentCount[i] + "" \
                     "}) "
            session.run(query)
            idPost = DNN.Id[i]
            parentId = DNN.ParentId[i]
            queryRelation = "MATCH(a: Post), (b:Post) WHERE a.IdPost = '" + idPost + "' AND b.IdPost = '" + parentId + \
                            "' CREATE(a) - [r: ANSWER {name: a.IdPost + '->' + b.IdPost}]->(b) RETURN type(r), r.name "
            session.run(queryRelation)

