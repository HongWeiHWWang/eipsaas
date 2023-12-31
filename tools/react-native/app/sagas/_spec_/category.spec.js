
import { put, call } from 'redux-saga/effects';
import * as store from '../../utils/StoreUtil';
import { requestTypeList } from '../category';
import { fetchTypeList, receiveTypeList } from '../../actions/category';
import RequestUtil from '../../utils/RequestUtil';
import { WEXIN_ARTICLE_TYPE } from '../../constants/Urls';

/* global expect */
describe('category saga tests', () => {
  const generator = requestTypeList();
  const step = input => generator.next(input).value;
  const mockTypeList = {
    showapi_res_body: {
      typeList: [
        {
          id: '19',
          name: 'Sports'
        },
        {
          id: '2',
          name: 'Entertainment'
        }
      ]
    }
  };

  it('should put(fetchTypeList())', () => {
    const next = step();
    expect(next).toEqual(put(fetchTypeList()));
  });

  it("should call(request, WEXIN_ARTICLE_TYPE, 'get')", () => {
    const next = step();
    expect(next).toEqual(call(RequestUtil.request, WEXIN_ARTICLE_TYPE, 'get'));
  });

  it('should put(receiveTypeList(typeList.showapi_res_body.typeList))', () => {
    const next = step(mockTypeList);
    expect(next).toEqual(put(receiveTypeList(mockTypeList.showapi_res_body.typeList)));
  });

  it("should call(store.save, 'typeList', typeList.showapi_res_body.typeList)", () => {
    const next = step(mockTypeList);
    expect(next).toEqual(call(store.save, 'typeList', mockTypeList.showapi_res_body.typeList));
  });

  it('should be done', () => {
    const next = generator.next();
    expect(next.done).toEqual(true);
  });
});
