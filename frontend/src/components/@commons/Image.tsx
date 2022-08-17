import styled from 'styled-components';
import { ImageSizeType } from 'types';

const Image = ({ src, margin = '0', sizes = 'LARGE' }: ImageProps) => {
  return <ImageStyle src={src} sizes={sizes} margin={margin} />;
};

interface ImageProps {
  src: string;
  sizes: ImageSizeType;
  margin?: string;
}

const ImageStyle = styled.img<{ margin: string }>`
  width: ${(props) => props.theme.imageSize[props.sizes!]};
  height: ${(props) => props.theme.imageSize[props.sizes!]};
  margin: ${(props) => props.margin};
  border-radius: ${(props) => props.theme.imageSize[props.sizes!]};
`;

export default Image;
