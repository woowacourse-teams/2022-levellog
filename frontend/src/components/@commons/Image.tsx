import { ImageSize } from 'levellog-util-types';
import styled from 'styled-components';

const Image = ({
  src,
  sizes = 'LARGE',
  boxShadow = false,
  borderRadius = true,
  githubAvatarSize = 0,
}: ImageProps) => {
  return (
    <ImageStyle
      src={githubAvatarSize ? `${src}&s=${githubAvatarSize}` : src}
      sizes={sizes}
      boxShadow={boxShadow}
      borderRadius={borderRadius}
    />
  );
};

interface ImageProps {
  src: string;
  sizes: keyof ImageSize;
  boxShadow?: boolean;
  borderRadius?: boolean;
  githubAvatarSize?: number;
}

const ImageStyle = styled.img<{ boxShadow: boolean; borderRadius: boolean }>`
  width: ${(props) => props.theme.imageSize[props.sizes!]};
  height: ${(props) => props.theme.imageSize[props.sizes!]};
  box-shadow: ${(props) =>
    props.boxShadow && '0.0625rem 0.0625rem 0.3125rem ${(props) => props.theme.new_default.GRAY}'};
  border-radius: ${(props) => props.borderRadius && props.theme.imageSize[props.sizes!]};
`;

export default Image;
