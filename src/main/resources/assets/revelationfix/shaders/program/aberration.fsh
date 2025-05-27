#version 150

uniform sampler2D DiffuseSampler;

uniform float Blur;
uniform float FallOff;
in vec2 texCoord;
in vec2 oneTexel;

out vec4 fragColor;
void main() 
{
	float distance = pow(distance(texCoord.st, vec2(0.5)), 3.0);
    float f = Blur + FallOff;

    vec2 destCoord = texCoord;
    vec2 direction = normalize(destCoord - 0.5);
    vec2 velocity = direction * Blur * pow(length(destCoord - 0.5), FallOff);
    float inverseSampleCount = 1.0 / 50.0;

    vec2 v1 = velocity * 1.0 * inverseSampleCount;
    vec2 v2 = v1 * 2.0;
    vec2 v3 = v1 * 4.0;
    mat3x2 increments = mat3x2(v1.x, v1.y,
    v1.x*2.0, v1.y*2.0,
    v1.x*4.0, v1.y*4.0);

    vec3 accumulator = vec3(0);
    mat3x2 offsets = mat3x2(0);

    for (int i = 0; i < 50; i++) {
        accumulator.r += texture(DiffuseSampler, destCoord + offsets[0]).r;
        accumulator.g += texture(DiffuseSampler, destCoord + offsets[1]).g;
        accumulator.b += texture(DiffuseSampler, destCoord + offsets[2]).b;

        offsets -= increments;
    }

    fragColor  = vec4(accumulator / 50.0, 1.0);
}

