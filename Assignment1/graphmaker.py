import pandas as pd
import seaborn as sns
import matplotlib.pyplot as plt
df = pd.read_csv("Datas/data_random.csv")
dfInt = df[df.Type=="Integer"]
dfByte = df[df.Type=="Byte"]
dfString = df[df.Type=="String"]

df100Int = dfInt[dfInt.Size==100]
df1000Int = dfInt[dfInt.Size==1000]
df5000Int = dfInt[dfInt.Size==5000]
ax100Int = sns.catplot(data=df100Int, x="Algorithm", y="Time (nanoseconds)", kind="box").set(ylabel='Test')
ax100Int.set(ylim=[0,1000000])
ax1000Int = sns.catplot(data=df1000Int, x="Algorithm", y="Time (nanoseconds)", kind="box")
ax5000Int = sns.catplot(data=df5000Int, x="Algorithm", y="Time (nanoseconds)", kind="box")

df100Byte = dfByte[dfByte.Size==100]
df1000Byte = dfByte[dfByte.Size==1000]
df5000Byte = dfByte[dfByte.Size==5000]
ax100Byte = sns.catplot(data=df100Byte, x="Algorithm", y="Time (nanoseconds)", kind="box")
ax1000Byte = sns.catplot(data=df1000Byte, x="Algorithm", y="Time (nanoseconds)", kind="box")
ax5000Byte = sns.catplot(data=df5000Byte, x="Algorithm", y="Time (nanoseconds)", kind="box")

df100String = dfString[dfString.Size==100]
df1000String = dfString[dfString.Size==1000]
df5000String = dfString[dfString.Size==5000]
ax100String = sns.catplot(data=df100String, x="Algorithm", y="Time (nanoseconds)", kind="box")
ax1000String = sns.catplot(data=df1000String, x="Algorithm", y="Time (nanoseconds)", kind="box")
ax5000String = sns.catplot(data=df5000String, x="Algorithm", y="Time (nanoseconds)", kind="box")

figureInt100 = ax100Int.fig
figureInt1000 = ax1000Int.fig
figureInt5000 = ax5000Int.fig

figureByte100 = ax100Byte.fig
figureByte1000 = ax1000Byte.fig
figureByte5000 = ax5000Byte.fig

figureString100 = ax100String.fig
figureString1000 = ax1000String.fig
figureString5000 = ax5000String.fig

figureInt100.savefig("./graphs/figureIntRandom100.png")
figureInt1000.savefig("./graphs/figureIntRandom1000.png")
figureInt5000.savefig("./graphs/figureIntRandom5000.png")

figureByte100.savefig("./graphs/figureByteRandom100.png")
figureByte1000.savefig("./graphs/figureByteRandom1000.png")
figureByte5000.savefig("./graphs/figureByteRandom5000.png")

figureString100.savefig("./graphs/figureStringRandom100.png")
figureString1000.savefig("./graphs/figureStringRandom1000.png")
figureString5000.savefig("./graphs/figureStringRandom5000.png")
