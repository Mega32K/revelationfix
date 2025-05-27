#version 150

uniform sampler2D DiffuseSampler;

in vec2 texCoord;
in vec2 oneTexel;

uniform float GameTime;
uniform vec2 Frequency;
uniform vec2 WobbleAmount;
uniform float Alpha;

out vec4 fragColor;

#define PI 3.14159265359


vec4 generic_desaturate(vec3 color, float factor)
{
    vec3 lum = vec3(0.299, 0.587, 0.114);
    vec3 gray = vec3(dot(lum, color));
    return vec4(mix(color, gray, factor), 1.0);
}


vec3 bwoow(vec2 uv) {
    float bwoow = (sin(clamp(0.0,1.0,GameTime / 2.0) * 3.1415926)) * 2.0;

    float sinewave = sin(clamp(0.0,1.0,length(uv-0.5)) * 2.0);

    float bwoow2ElectricBoogaloo = (1.0-sinewave) * bwoow / 4.0;
    vec2 scale = (uv-0.5) * (1.0-bwoow2ElectricBoogaloo-1.0);

    return vec3(scale,bwoow);
}
float random (in vec2 st) {
    return fract(sin(dot(st.xy,
    vec2(12.9898,78.233)))
    * 43758.5453123);
}
float noise (in vec2 st) {
    vec2 i = floor(st);
    vec2 f = fract(st);

    // Four corners in 2D of a tile
    float a = random(i);
    float b = random(i + vec2(1.0, 0.0));
    float c = random(i + vec2(0.0, 1.0));
    float d = random(i + vec2(1.0, 1.0));

    // Smooth Interpolation

    // Cubic Hermine Curve.  Same as SmoothStep()
    vec2 u = f*f*(3.0-2.0*f);
    // u = smoothstep(0.,1.,f);

    // Mix 4 coorners percentages
    return mix(a, b, u.x) +
    (c - a)* u.y * (1.0 - u.x) +
    (d - b) * u.x * u.y;
}
float scanline(vec2 uv, float squish,float epsilon) {
    float show = floor(noise(uv * vec2(5.0,50.0))+epsilon);
    float vertical = cos(uv.y*PI*2.0 * squish + PI)*.5+.5;
    return vertical * show;
}
float weirdVignette(vec2 uv, float power) {
    float dist = distance(uv,vec2(0.5,0.5));
    return 1.0 / (dist * power + 1.0);
}
float distortion2(vec2 uv) {
    float one = noise(uv * vec2(0.0,10.0) + GameTime * 2.0) / 1.0;
    float two = noise(uv * vec2(0.0,20.0) + GameTime * 8.0) / 2.0;
    return one + two;
}
float d(vec3 color )
{
    vec3 lum = vec3(0.299, 0.587, 0.114);
    return color.x * lum.x + color.y*lum.y + color.z*lum.z;
}
void main() { // Normalized pixel coordinates (from 0 to 1)
    vec2 uv = texCoord;

    vec3 distort = bwoow(uv);

    float shakeSpeed = GameTime * 100.0;
    float shakeSize = 0.002;
    vec2 shake = vec2(sin(shakeSpeed),cos(shakeSpeed)) * shakeSize * sqrt(distort.z);

    float lines = scanline(uv +GameTime * 50.0 * vec2(1.0,2.0) * 1.0,20.0,0.05);

    float timeEffects = smoothstep(0.0,1.0,GameTime);
    float timeEffects2 = smoothstep(0.0,3.0,GameTime);

    float distortion = ((distortion2(uv) / 1.5) / 16.0 - 1.0 / 32.0)* timeEffects2;
    // Output to screen
    vec4 rgba = texture(DiffuseSampler, uv + distortion * vec2(1.0,0.0) + distort.xy + shake);
    if (rgba.a < 0.1) discard;
    float d_ = d(rgba.rgb) > 0.0 ? 1.0 : 0.0;
    fragColor = Alpha*d_*(1.0-weirdVignette(uv,timeEffects2 * 2.0)) / 2.0 + weirdVignette(uv,timeEffects * 2.0)* d_ * generic_desaturate(rgba.rgb + lines * smoothstep(0.0,2.0,GameTime),smoothstep(0.0,1.0,GameTime) / 1.5);
    //fragColor = vec4(vec3(distortion),1.0);
}