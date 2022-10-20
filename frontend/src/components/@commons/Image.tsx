import styled from 'styled-components';
import { ImageSizeType } from 'types';

const Image = ({
  src,
  alt,
  sizes = 'LARGE',
  boxShadow = false,
  borderRadius = true,
  githubAvatarSize = 0,
  ...props
}: ImageProps) => {
  return (
    <ImageStyle
      src={githubAvatarSize ? `${src}&s=${githubAvatarSize}` : src}
      alt={alt}
      sizes={sizes}
      boxShadow={boxShadow}
      borderRadius={borderRadius}
      {...props}
    />
  );
};

export interface ImageProps {
  src: string | undefined;
  alt?: string;
  sizes: ImageSizeType;
  boxShadow?: boolean;
  borderRadius?: boolean;
  githubAvatarSize?: number;
}

const ImageStyle = styled.img<{ boxShadow: boolean; borderRadius: boolean }>`
  width: ${(props) => props.theme.imageSize[props.sizes!].width};
  height: ${(props) => props.theme.imageSize[props.sizes!].height};
  box-shadow: ${(props) =>
    props.boxShadow && '0.0625rem 0.0625rem 0.3125rem ${(props) => props.theme.new_default.GRAY}'};
  border-radius: ${(props) => props.borderRadius && props.theme.imageSize[props.sizes!].width};
`;

export default Image;
