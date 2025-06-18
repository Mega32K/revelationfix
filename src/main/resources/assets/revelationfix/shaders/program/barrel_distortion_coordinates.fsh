#version 150

uniform sampler2D DiffuseSampler;
uniform float Distortion;
uniform vec2 CenterOffset;

in vec2 texCoord;
in vec2 oneTexel;

out vec4 fragColor;

vec2 barrelDistortionCoordinates(vec2 uv){
    vec2 pos = 2.0 * uv - 1.0;

    float len = distance(pos, vec2(0.0)) ;
    len = len*Distortion;

    pos = pos + pos * len * len;
    pos = 0.5 * (pos / (Distortion+1.0) + 1.0);

    return pos;
}
void main()
{
    vec2 uv = barrelDistortionCoordinates(texCoord+CenterOffset*Distortion);
    vec3 color = vec3(0.0);
    color.r = texture(DiffuseSampler, uv + vec2(Distortion*0.02, Distortion*-0.02)).r;
    color.g = texture(DiffuseSampler, uv + vec2(Distortion*-0.02, Distortion*0.02)).g;
    color.b = texture(DiffuseSampler, uv + vec2(Distortion*-0.02)).b;
    fragColor = vec4(color, 1.0);
}

