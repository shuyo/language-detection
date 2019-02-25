# language-detector

### What It is?
It can detect different language by a string;

### How to Use

1. maven build or download 'lib' file's three jar package import to your project
import maven dependency

```
<!-- https://mvnrepository.com/artifact/com.cybozu.labs/langdetect -->
<dependency>
    <groupId>com.cybozu.labs</groupId>
    <artifactId>langdetect</artifactId>
    <version>1.1-20120112</version>
</dependency>

```

or <br/>

download 'lib' file's three jar package import to your project,as libraries; 


2. use like this

>Attention:
Because the jar package is not update, you need copy 'profiles' file to you project classpath.

```
//you load yourself profile,if you don't have it or make it, you can use default,just `Detector dectector1 = DetectorFactory.create()`,then you can use default profile
DetectorFactory.loadProfile("profile");
Detector detector = DetectorFactory.create();
detector.append("need be detected string");
//this is detect result
String language = detector.detect();     
```


###  Default Language Suppport

<table border="1">
<tr><td>1</td><td>af</td><td> Afrikaans</td><td>南非語</td></tr>
<tr><td>2</td><td>ar</td><td> Arabic</td><td>阿拉伯语</td></tr>
<tr><td>3</td><td>bg</td><td> Bulgarian</td><td>保加利亚语</td></tr>
<tr><td>4</td><td>bn</td><td> Bengali</td><td>孟加拉语</td></tr>
<tr><td>5</td><td>cs</td><td> Czech</td><td>捷克语</td></tr>
<tr><td>6</td><td>da</td><td> Danish</td><td>丹麦语</td></tr>
<tr><td>7</td><td>de</td><td> German</td><td>德语</td></tr>
<tr><td>8</td><td>el</td><td> Greek</td><td>现代希腊语</td></tr>
<tr><td>9</td><td>en</td><td> English</td><td>英语</td></tr>
<tr><td>10</td><td>es</td><td> Spanish</td><td>西班牙语</td></tr>
<tr><td>11</td><td>et</td><td> Estonian</td><td>爱沙尼亚语</td></tr>
<tr><td>12</td><td>fa</td><td> Persian</td><td>波斯语</td></tr>
<tr><td>13</td><td>fi</td><td> Finnish</td><td>芬兰语</td></tr>
<tr><td>14</td><td>fr</td><td> French</td><td>法语</td></tr>
<tr><td>15</td><td>gu</td><td> Gujarati</td><td>古吉拉特语</td></tr>
<tr><td>16</td><td>he</td><td>Hebrew</td><td>希伯来语</td></tr>
<tr><td>17</td><td>hi</td><td> Hindi</td><td>印地语</td></tr>
<tr><td>18</td><td>hr</td><td> Croatian</td><td>克罗地亚语</td></tr>
<tr><td>19</td><td>hu</td><td> Hungarian</td><td>匈牙利语</td></tr>
<tr><td>20</td><td>id</td><td> Indonesian</td><td>印尼语</td></tr>
<tr><td>21</td><td>it</td><td> Italian</td><td>意大利语</td></tr>
<tr><td>22</td><td>ja</td><td> Japanese</td><td>日语</td></tr>
<tr><td>23</td><td>kn</td><td> Kannada</td><td>卡纳达语</td></tr>
<tr><td>24</td><td>ko</td><td> Korean</td><td>朝鲜语、韩语</td></tr>
<tr><td>25</td><td>lt</td><td> Lithuanian</td><td>立陶宛语</td></tr>
<tr><td>26</td><td>lv</td><td> Latvian</td><td>拉脱维亚语</td></tr>
<tr><td>27</td><td>mk</td><td> Macedonian</td><td>马其顿语</td></tr>
<tr><td>28</td><td>ml</td><td>Malayalam </td><td>马拉亚拉姆语</td></tr>
<tr><td>29</td><td>mr</td><td>Marathi </td><td>马拉地语</td></tr>
<tr><td>30</td><td>ne</td><td>Nepali</td><td>尼泊尔语</td></tr>
<tr><td>31</td><td>nl</td><td> Dutch</td><td>荷兰语</td></tr>
<tr><td>32</td><td>no</td><td> Norwegian</td><td>挪威语</td></tr>
<tr><td>33</td><td>pa</td><td>Punjab</td><td>旁遮普语</td></tr>
<tr><td>34</td><td>pl</td><td> Polish</td><td>波兰语</td></tr>
<tr><td>35</td><td>pt</td><td> Portuguese</td><td>葡萄牙语</td></tr>
<tr><td>36</td><td>ro</td><td> Romanian</td><td>罗马尼亚语</td></tr>
<tr><td>37</td><td>ru</td><td> Russian</td><td>俄语</td></tr>
<tr><td>38</td><td>sk</td><td> Slovak</td><td>斯洛伐克语</td></tr>
<tr><td>39</td><td>sl</td><td> Slovenian</td><td>斯洛文尼亚语</td></tr>
<tr><td>40</td><td>so</td><td>Af-Soomaali</td><td>索马里语</td></tr>
<tr><td>41</td><td>sq</td><td> Albanian</td><td>阿尔巴尼亚语</td></tr>
<tr><td>42</td><td>sv</td><td> Swedish</td><td>瑞典语</td></tr>
<tr><td>43</td><td>sw</td><td> Swahili</td><td>斯瓦希里语</td></tr>
<tr><td>44</td><td>ta</td><td> Tamil</td><td>泰米尔语</td></tr>
<tr><td>45</td><td>te</td><td> Telugu</td><td>泰卢固语</td></tr>
<tr><td>46</td><td>th</td><td> Thai</td><td>泰语</td></tr>
<tr><td>47</td><td>tl</td><td> Filipino</td><td>他加禄语</td></tr>
<tr><td>48</td><td>tr</td><td> Turkish</td><td>土耳其语</td></tr>
<tr><td>49</td><td>uk</td><td> Ukrainian</td><td>乌克兰语</td></tr>
<tr><td>50</td><td>ur</td><td> Urdu</td><td>乌尔都语</td></tr>
<tr><td>51</td><td>vi</td><td> Vietnamese</td><td>越南语</td></tr>
<tr><td>52</td><td>zh-cn</td><td> Chinese Simplified</td><td>中文简体</td></tr>
<tr><td>53</td><td>zh-tw</td><td> Chinese Traditional</td><td>中文繁体</td></tr>
</table>