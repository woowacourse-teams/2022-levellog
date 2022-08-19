import styled from 'styled-components';
import { ImageSizeType } from 'types';

const Image = ({ src, sizes = 'LARGE', boxShadow = false, borderRadius = true }: ImageProps) => {
  return <ImageStyle src={src} sizes={sizes} boxShadow={boxShadow} borderRadius={borderRadius} />;
};

interface ImageProps {
  src: string;
  sizes: ImageSizeType;
  boxShadow?: boolean;
  borderRadius?: boolean;
}

const ImageStyle = styled.img<{ boxShadow: boolean; borderRadius: boolean }>`
  width: ${(props) => props.theme.imageSize[props.sizes!]};
  height: ${(props) => props.theme.imageSize[props.sizes!]};
  box-shadow: ${(props) =>
    props.boxShadow && '0.0625rem 0.0625rem 0.3125rem ${(props) => props.theme.new_default.GRAY}'};
  border-radius: ${(props) => props.borderRadius && props.theme.imageSize[props.sizes!]};
`;

export default Image;
