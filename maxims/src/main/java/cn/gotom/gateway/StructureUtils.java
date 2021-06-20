package cn.gotom.gateway;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import cn.gotom.commons.entities.Structure;

public class StructureUtils {

	public static List<Structure> tree(List<Structure> sortList) {
		return tree(sortList, null);
	}

	public static List<Structure> tree(final List<Structure> sortList, Structure top) {
		List<Structure> topList;
		if (top != null) {
			topList = sortList.stream()//
					.filter(structure -> top.getId().equals(structure.getParentId()))//
					.collect(Collectors.toList());
		} else {
			topList = sortList.stream()//
					.filter(structure -> StringUtils.isBlank(structure.getParentId()))//
					.collect(Collectors.toList());
		}
		sortList.removeAll(topList);
		topList.forEach(structure -> {
			treeCallback(structure, sortList);
		});
		return topList;
	}

	private static void treeCallback(Structure parent, List<Structure> structureList) {
		List<Structure> children = structureList.stream()//
				.filter(r -> parent.getId().equals(r.getParentId()))// 过虑
				.collect(Collectors.toList());
		structureList.removeAll(children);
		parent.setChildren(children);
		children.forEach(child -> {
			treeCallback(child, structureList);
		});
	}

	public static List<Structure> tree2List(List<Structure> treeList) {
		List<Structure> structureList = new ArrayList<>();
		tree2ListCallback(treeList, structureList);
		return structureList;
	}

	private static void tree2ListCallback(List<Structure> treeList, List<Structure> structureList) {
		for (Structure structure : treeList) {
			structureList.add(structure);
			if (structure.getChildren() != null && structure.getChildren().size() > 0) {
				tree2ListCallback(structure.getChildren(), structureList);
			}
			structure.setChildren(null);
		}
	}

//
//	public static List<Structure> cutTree(List<Structure> treeList, List<Structure> nodeList) {
//		for (Structure node : nodeList) {
//			cutTree(treeList, node);
//		}
//		List<Structure> tmpList = new ArrayList<>(nodeList);
//		for (int i = 0; i < nodeList.size(); i++) {
//			Structure node = nodeList.get(i);
//			tmpList.remove(node);
//			if (!treeIndex(tmpList, node)) {
//				
//			}
//		}
//		return tmpList;
//	}
//
//	private static boolean treeIndex(List<Structure> treeList, Structure node) {
//		for (Structure structure : treeList) {
//			if (structure.equals(node)) {
//				return true;
//			} else if (!CollectionUtils.isEmpty(structure.getChildren())) {
//				return treeIndex(structure.getChildren(), node);
//			}
//		}
//		return false;
//	}

	public static void removeNode(List<Structure> treeList, String removeId) {
		for (Structure structure : treeList) {
			if (removeId.equals(structure.getId())) {
				treeList.remove(structure);
				return;
			} else if (!CollectionUtils.isEmpty(structure.getChildren())) {
				removeNode(structure.getChildren(), removeId);
			}
		}
	}
}
