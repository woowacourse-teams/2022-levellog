import styled from 'styled-components';
import { ImageSizeType } from 'types';

const Image = ({ src, margin = '0', sizes = 'LARGE', boxShadow = false }: ImageProps) => {
  return <ImageStyle src={src} sizes={sizes} margin={margin} boxShadow={boxShadow} />;
};

interface ImageProps {
  src: string;
  sizes: ImageSizeType;
  margin?: string;
  boxShadow?: boolean;
}

const ImageStyle = styled.img<{ margin: string; boxShadow: boolean }>`
  width: ${(props) => props.theme.imageSize[props.sizes!]};
  height: ${(props) => props.theme.imageSize[props.sizes!]};
  margin: ${(props) => props.margin};
  box-shadow: ${(props) =>
    props.boxShadow && '0.0625rem 0.0625rem 0.3125rem ${(props) => props.theme.new_default.GRAY}'};
  border-radius: ${(props) => props.theme.imageSize[props.sizes!]};
`;

export default Image;
