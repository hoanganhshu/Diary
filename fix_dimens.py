import re

# Mapping từ tên dimension về giá trị dp thực tế
# Tên dimension: giá trị dp
dim_mapping = {
    "_0sdp": "0dp",
    "_1sdp": "1dp",
    "_1_5sdp": "1.5dp",
    "_2sdp": "2dp",
    "_3sdp": "3dp",
    "_4sdp": "4dp",
    "_5sdp": "5dp",
    "_6sdp": "6dp",
    "_7sdp": "7dp",
    "_8sdp": "8dp",
    "_9_21sdp": "9.21dp",
    "_10sdp": "10dp",
    "_11sdp": "11dp",
    "_12sdp": "12dp",
    "_13sdp": "13dp",
    "_14sdp": "14dp",
    "_15sdp": "15dp",
    "_16sdp": "16dp",
    "_17sdp": "17dp",
    "_18sdp": "18dp",
    "_19sdp": "19dp",
    "_20sdp": "20dp",
    "_21sdp": "21dp",
    "_22sdp": "22dp",
    "_23sdp": "23dp",
    "_24sdp": "24dp",
    "_24_62sdp": "24.62dp",
    "_25sdp": "25dp",
    "_27sdp": "27dp",
    "_28sdp": "28dp",
    "_29sdp": "29dp",
    "_30sdp": "30dp",
    "_31sdp": "31dp",
    "_32sdp": "32dp",
    "_33sdp": "33dp",
    "_34sdp": "34dp",
    "_36sdp": "36dp",
    "_37sdp": "37dp",
    "_38sdp": "38dp",
    "_40sdp": "40dp",
    "_42sdp": "42dp",
    "_43sdp": "43dp",
    "_48sdp": "48dp",
    "_49sdp": "49dp",
    "_50sdp": "50dp",
    "_55sdp": "55dp",
    "_56sdp": "56dp",
    "_58sdp": "58dp",
    "_60sdp": "60dp",
    "_64sdp": "64dp",
    "_65_5sdp": "65.5dp",
    "_70sdp": "70dp",
    "_72sdp": "72dp",
    "_73sdp": "73dp",
    "_80sdp": "80dp",
    "_84sdp": "84dp",
    "_85_63sdp": "85.63dp",
    "_86sdp": "86dp",
    "_90sdp": "90dp",
    "_96sdp": "96dp",
    "_100sdp": "100dp",
    "_108sdp": "108dp",
    "_114sdp": "114dp",
    "_120sdp": "120dp",
    "_130sdp": "130dp",
    "_132sdp": "132dp",
    "_138sdp": "138dp",
    "_140sdp": "140dp",
    "_141sdp": "141dp",
    "_142sdp": "142dp",
    "_150sdp": "150dp",
    "_160sdp": "160dp",
    "_174sdp": "174dp",
    "_179sdp": "179dp",
    "_180sdp": "180dp",
    "_187sdp": "187dp",
    "_200sdp": "200dp",
    "_208sdp": "208dp",
    "_220sdp": "220dp",
    "_234sdp": "234dp",
    "_240sdp": "240dp",
    "_246sdp": "246dp",
    "_260sdp": "260dp",
    "_264sdp": "264dp",
    "_280sdp": "280dp",
    "_301sdp": "301dp",
    "_321sdp": "321dp",
    "_330sdp": "330dp",
    "_351sdp": "351dp",
    "_400sdp": "400dp",
    "_403sdp": "403dp",
    "_999sdp": "999dp",
}

# Đọc file dimens.xml
with open("app/src/main/res/values/dimens.xml", "r", encoding="utf-8") as f:
    content = f.read()

# Tạo nội dung mới
new_content = '<?xml version="1.0" encoding="utf-8"?>\n\n<resources>\n'
new_content += '    <dimen name="dot_base">8dp</dimen>\n'
new_content += '    <dimen name="dot_spacing">8dp</dimen>\n'
new_content += '    <dimen name="image_selected_stroke_width">2dp</dimen>\n\n'

# Thêm các dimension với giá trị dp thực tế
for dim_name in sorted(dim_mapping.keys()):
    dp_value = dim_mapping[dim_name]
    new_content += f'    <dimen name="{dim_name}">{dp_value}</dimen>\n'

new_content += '\n</resources>\n'

# Ghi lại file
with open("app/src/main/res/values/dimens.xml", "w", encoding="utf-8") as f:
    f.write(new_content)

print("Đã sửa dimens.xml thành công!")

