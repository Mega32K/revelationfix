#version 150

uniform sampler2D DiffuseSampler;

uniform float Multiplier;
uniform vec2 Multiplier2;

in vec2 texCoord;
in vec2 oneTexel;

out vec4 fragColor;

void main()
{
    vec2 tc = texCoord * Multiplier2;
    float x2 = (tc.x - 0.5)*(tc.x - 0.5);
    float y2 = (tc.y - 0.5)*(tc.y - 0.5);

    float distance = pow(sqrt(x2+y2/1.5F), 3.0);

    vec4 rValue = texture(DiffuseSampler, tc.st + vec2(Multiplier * distance, 0.0));
    vec4 gValue = texture(DiffuseSampler, tc.st);
    vec4 bValue = texture(DiffuseSampler, tc.st - vec2(Multiplier * distance, 0.0));

    fragColor = vec4(rValue.r, gValue.g, bValue.b, 1.0);
}

