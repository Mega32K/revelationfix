#version 150

uniform sampler2D DiffuseSampler;

in vec2 texCoord;
in vec2 oneTexel;

uniform vec2 InSize;

uniform mat4 ProjMat;
uniform vec3 Gray;
uniform vec3 RedMatrix;
uniform vec3 GreenMatrix;
uniform vec3 BlueMatrix;
uniform vec3 Offset;
uniform vec3 ColorScale;
uniform float Saturation;
uniform vec4 HaloPos;
uniform float Scale;

out vec4 fragColor;

void main() {
    vec4 InTexel = texture(DiffuseSampler, texCoord);

    // Color Matrix
    float RedValue = dot(InTexel.rgb, RedMatrix);
    float GreenValue = dot(InTexel.rgb, GreenMatrix);
    float BlueValue = dot(InTexel.rgb, BlueMatrix);
    vec3 OutColor = vec3(RedValue, GreenValue, BlueValue);

    // Offset & Scale
    OutColor = (OutColor * ColorScale) + Offset;

    // Saturation
    float Luma = dot(OutColor, Gray);
    vec3 Chroma = OutColor - Luma;
    //te item pos modify
    vec4 position = ProjMat * HaloPos;
    float Sat = mix(Saturation, 1.0, 1.0-clamp(distance(position.xy, texCoord) * Scale * 4.0, 0.0, 1.0));
    OutColor = (Chroma * Sat) + Luma;

    fragColor = vec4(OutColor, 1.0);
}
