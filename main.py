from PIL import Image
from colormap import rgb2hex
import math

def open_image(path):
    res = Image.open(path)
    width, height = res.size
    res = res.resize((math.floor(width/5),math.floor(height/5)))
    width, height = res.size
    return res, width, height

def harvest_pixels(image):
    stage1 = []
    for px in image.getdata():
        x = int(str(px).strip('()').split(',')[0]),int(str(px).strip('()').split(',')[1]),int(str(px).strip('()').split(',')[2])
        stage1.append((str(x).replace('[','(').replace(']',')')))
    res = []
    for px in stage1:
        res.append(rgb2hex(int(str(px).strip('()').split(',')[0]),int(str(px).strip('()').split(',')[1]),int(str(px).strip('()').split(',')[2])))
    return res

def add_location_to_pixels(pixels, width, height):
    k=0
    res_list=[]
    for i in range(height):
        for j in range(width):
            if(k+1 == len(pixels)):
                res_list.append(str(j)+'px '+str(i)+'px 1px 1px '+str(pixels[k]))
            else:
                res_list.append(str(j)+'px '+str(i)+'px 1px 1px '+str(pixels[k])+',')
            k+=1
    return res_list

def create_html_file(rendered_pixels):
    style = open('output/generated_css_code_from_jpg.html','w')
    style.write('<style>\n#drawing{\n\tposition:absolute;\n\tbox-shadow:\n')
    for e in rendered_pixels:
        style.write('\t\t'+str(e)+'\n')
    style.write('}\n</style>')
    style.write('<body><div id="drawing"></div></body>')
    style.close()

def main():
    image, width, height = open_image("images/cat.jpg")
    pixels = harvest_pixels(image)
    rendered_pixels = add_location_to_pixels(pixels, width, height)
    create_html_file(rendered_pixels)

if __name__ == "__main__":
    main()
