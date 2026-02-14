#version 150
#define FROSTYNESS 0.5
#define RANDNERF 2.5

uniform sampler2D LichenSampler;
uniform sampler2D FadeSampler;
uniform sampler2D DiffuseSampler;
uniform float Percent;
uniform float SeriousTotalTime;
uniform vec3 Center;
uniform float Distance;
uniform vec2 OutSize;

in vec2 texCoord;
in vec2 oneTexel;

out vec4 fragColor;

float rand(vec2 uv) {
    #ifdef RANDNERF
    uv = floor(uv*pow(10.0, RANDNERF))/pow(10.0, RANDNERF);
    #endif

    float a = dot(uv, vec2(92., 80.));
    float b = dot(uv, vec2(41., 62.));

    float x = sin(a) + cos(b) * 51.;
    return fract(x);
}

vec4 theEndRing() {
    if (Center.z < 0.0) {
        return texture(DiffuseSampler, texCoord);
    }
    vec2 invAr = vec2(1.0, OutSize.y / OutSize.x);
    vec2 uv = texCoord;
    float progress = clamp(Percent * Distance, 0.0, 0.999999) * min(SeriousTotalTime / 4.0, 1.0);

    vec4 frost = texture(LichenSampler, uv + vec2(sin(SeriousTotalTime * 3.) * 0.01, SeriousTotalTime * 0.5));
    float icespread = texture(FadeSampler, uv + vec2(SeriousTotalTime * 0.3 + cos((uv.x+SeriousTotalTime) * 3.) * 0.01)).r;

    vec2 rnd = vec2(rand(uv+frost.r*0.05), rand(uv+frost.b*0.05));

    float size = mix(progress, sqrt(progress), 0.5);
    size = size * 1.12 + 0.0000001; // just so 0.0 and 1.0 are fully (un)frozen and i'm lazy

    vec2 lens = vec2(size, pow(size, 4.0) / 2.0);
    float dist = distance(uv.xy*invAr, Center.xy*invAr); // the center of the froziness
    float vignette = pow(1.0-smoothstep(lens.x, lens.y, dist), 2.0);

    rnd *= frost.rg*vignette*FROSTYNESS;

    rnd *= 1.0 - floor(vignette); // optimization - brings rnd to 0.0 if it won't contribute to the image

    vec4 regular = texture(DiffuseSampler, uv);
    vec4 frozen = texture(DiffuseSampler, uv + rnd);
    frozen *= vec4(0.8, 0.8, 1.1, 1.0);

    return mix(frozen, regular, smoothstep(icespread, 1.0, pow(vignette, 2.0)));
}

void main()
{
    fragColor = theEndRing();
    if (Percent > 0.001) {
        if (texCoord.y > 1.0-0.15*Percent || texCoord.y < 0.15*Percent) {
            fragColor = vec4(0.0);
        }
    }
}

