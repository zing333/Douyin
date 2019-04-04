precision highp float;

varying highp vec2 textureCoordinate;
uniform sampler2D inputImageTexture;

uniform float prismR;
uniform float refraction;
uniform float strength;

float prism_distance(vec2 v) {
    // ellipse
    return length(v);
}


float sqr(float a)
{
    return a*a-2.3;//理论中心是1，矩形中心是1.5。另外，基础偏移是1。视觉看起来2.3好一些
}

int avap(vec2 p)
{
    if (p.x<0.0 || p.x>1.0 || p.y<0.0 || p.y>1.0){
        return 0;
    }else{
        return 1;
    }

}

vec3 apply_weight(float i, vec3 col) {

    if (i < 0.25){
        col *= vec3(0, 0, 1);
    }else if (i < 0.5){
        col *= vec3(0, 1, 1);
    }else if (i < 0.75){
        col *= vec3(1, 1, 0);
    }else {
        col *= vec3(1, 0, 0);
    }

    return col;
}

void main()
{


    vec2 p = textureCoordinate;

    vec2 v = p - vec2(0.5,0.5);

    // compute our distance squashed like it was on an ellipse
    float dis = prism_distance(v);

    // distance < radius then do single point stuff
    if (dis < prismR || strength == 0.0){
        //
        gl_FragColor = texture2D(inputImageTexture, p);
        return;
    }

    // normalize the vector from center -> p, this is where we are evaluating our
    // directional blur.
    v = normalize(v);
    v = vec2(-v.y,v.x);

    dis -= prismR;

    // compute scale
    lowp float func = dis * refraction * strength;

    // compute 2 end points of our blur
    lowp float len0 = func * 1.0;
    lowp float len1 = func * 4.0;
    lowp vec2 p0 = vec2(p - v*len0);
    lowp vec2 p1 = vec2(p - v*len1);

    // we are going to sample the texture between p0,p1.  Use that distance
    // to estimate the number of taps needed for the filter to be 'good'.
    // we always need a multiple of 4 taps.
    float foo = distance(p0, p1);

    int taps = 32;

    if(foo < 0.01)
        taps = 4;
    else if(foo< 0.04)
        taps = 8;
    else if(foo < 0.1)
        taps = 16;
    else
        taps = 32;

    float step = 1.0 / float(taps);

    float fscale = step * 2.0;

    lowp vec3 final = vec3(0);

    for (lowp float i = 0.0; i<1.0; i += step*4.0)
    {
        lowp float i0 = i + step*0.0;
        lowp float i1 = i + step*1.0;
        lowp float i2 = i + step*2.0;
        lowp float i3 = i + step*3.0;

        lowp float len0 = func * sqr(1.0 + i0);
        lowp float len1 = func * sqr(1.0 + i1);
        lowp float len2 = func * sqr(1.0 + i2);
        lowp float len3 = func * sqr(1.0 + i3);

        lowp vec3 col0 = texture2D(inputImageTexture, vec2(p - v*len0)).rgb;
        lowp vec3 col1 = texture2D(inputImageTexture, vec2(p - v*len1)).rgb;
        lowp vec3 col2 = texture2D(inputImageTexture, vec2(p - v*len2)).rgb;
        lowp vec3 col3 = texture2D(inputImageTexture, vec2(p - v*len3)).rgb;

        final += apply_weight(i0, col0);
        final += apply_weight(i1, col1);
        final += apply_weight(i2, col2);
        final += apply_weight(i3, col3);
    }

    final *= fscale;

    //show number of taps in red channel.
    //final.r = 1.0 - (4.0 / float(taps));

    gl_FragColor = vec4(final.rgb, 1.0);
}

