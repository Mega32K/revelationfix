#version 150
uniform sampler2D DiffuseSampler;
uniform float Percent;

in vec2 texCoord;
in vec2 oneTexel;

out vec4 fragColor;

void main()
{
    fragColor = texture(DiffuseSampler, texCoord);
    if (Percent > 0.001) {
        if (texCoord.y > 1.0-0.15*Percent || texCoord.y < 0.15*Percent) {
            fragColor = vec4(0.0);
        }
    }
}

