import styled from 'styled-components';
import { ImageSizeType } from 'types';

const Image = ({ src, sizes = 'LARGE' }: ImageProps) => {
  return <ImageStyle src={src} sizes={sizes} />;
};

interface ImageProps {
  src: string;
  sizes: ImageSizeType;
}

const ImageStyle = styled.img`
  width: ${(props) => props.theme.imageSize[props.sizes]};
  height: ${(props) => props.theme.imageSize[props.sizes]};
  border-radius: ${(props) => props.theme.imageSize[props.sizes]};
`;

export default Image;
